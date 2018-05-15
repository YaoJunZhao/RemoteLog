package main

import (
	"bytes"
	"encoding/binary"
	"encoding/json"
	"fmt"
	"net"
	"os"
	"time"
)

func main() {
	tcpaddr, err := net.ResolveTCPAddr("tcp", ":8752")
	listener, err := net.ListenTCP("tcp", tcpaddr)
	if err != nil {
		fmt.Println("listen failed!")
		return
	}
	fmt.Println("listen start!")

	for {
		conn, err := listener.Accept()
		if err != nil {
			fmt.Println("Accept failed!")
			return
		}
		go dealClient(conn)
	}
}

func bytesToInt(b []byte) int {
	byteBuffer := bytes.NewBuffer(b)
	var x int32
	binary.Read(byteBuffer, binary.BigEndian, &x)
	return int(x)
}

type LogInfo struct {
	MsgType string `json:"msgtype"`
	MsgInfo string `json:"msginfo"`
}

func dealClient(conn net.Conn) {
	fmt.Println("user coming!")
	var fileName = ""
	for {
		info := make([]byte, 4)
		_, err := conn.Read(info)
		defer conn.Close()
		if err != nil {
			if err.Error() == "EOF" {
				fmt.Println("user disconnected!")
				return
			}
		} else {
			infolen := bytesToInt(info)
			msg := make([]byte, infolen)
			conn.Read(msg)

			var loginfo LogInfo
			json.Unmarshal(msg, &loginfo)

			currentTime := time.Now().Format("2006-01-02 15:04:05")
			if loginfo.MsgType == "2" {
				//生成文件名
				fileName = loginfo.MsgInfo
				fileName = fileName + "-" + currentTime + ".log"
				//fmt.Println(fileName)
			} else {
				// 写入文件
				log := currentTime + " " + loginfo.MsgInfo

				var filePath = "./" + fileName
				var f *os.File
				var err1 error
				if !checkFileIsExist(filePath) {
					f, err1 = os.Create(filePath)
					if err1 != nil {
						fmt.Println("create file error!")
					}
				} else {
					f, err1 = os.OpenFile(filePath, os.O_APPEND|os.O_WRONLY, 0666)
					if err1 != nil {
						fmt.Println("open file error!")
					}
				}
				_, err2 := f.WriteString(log + "\n")
				if err2 != nil {
					fmt.Println("write file error!" + err2.Error())
				}
				f.Sync()
				f.Close()
			}
		}
	}
}

func checkFileIsExist(filename string) bool {
	var exist = true
	if _, err := os.Stat(filename); os.IsNotExist(err) {
		exist = false
	}
	return exist
}
