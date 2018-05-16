package main

import (
	"encoding/json"
	"fmt"
	"net/http"
	"net/url"
	"os"
	"path/filepath"
)

type FileInfo struct {
	Name     string `json:filename`
	FilePath string `json:filepath`
}

type LogFile struct {
	FileList []FileInfo `json:filelist`
}

func getFilelist(path string) LogFile {
	var info LogFile
	filepath.Walk(path, func(path string, f os.FileInfo, err error) error {
		if f == nil {
			return err
		}
		if f.IsDir() {
			return nil
		}
		info.FileList = append(info.FileList, FileInfo{Name: f.Name(), FilePath: path})
		return nil
	})
	return info
}

func enumFile(w http.ResponseWriter, r *http.Request) {
	logFileInfo := getFilelist("../logdir/")
	for i := 0; i < len(logFileInfo.FileList); i++ {
		fmt.Println(logFileInfo.FileList[i].Name)
	}
	bytes, _ := json.Marshal(logFileInfo)
	fmt.Fprintf(w, string(bytes))
}

func getLogFile(w http.ResponseWriter, r *http.Request) {
	queryForm, err := url.ParseQuery(r.URL.RawQuery)
	if err == nil && len(queryForm["filename"]) > 0 {

		//read log file
		logdir := "../logdir/"
		fileName := queryForm["filename"][0]
		filePath := logdir + string(fileName)
		fileinfo, err := os.Stat(filePath)
		if os.IsNotExist(err) {
			fmt.Fprintf(w, "file not exist!")
		}
		f, err1 := os.OpenFile(logdir+string(fileName), os.O_RDONLY, 0666)
		if err1 != nil {
			fmt.Println("open file error!")
		}
		logbyte := make([]byte, fileinfo.Size())
		f.Read(logbyte)
		f.Close()
		fmt.Fprintf(w, string(logbyte))
	}
}

func main() {
	http.Handle("/", http.FileServer(http.Dir("../tmp/static/")))
	http.HandleFunc("/file", enumFile)
	http.HandleFunc("/logfileindex", getLogFile)
	http.ListenAndServe(":8080", nil)
}
