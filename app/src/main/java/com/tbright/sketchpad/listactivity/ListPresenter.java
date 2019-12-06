package com.tbright.sketchpad.listactivity;

public class ListPresenter {
    /**
     * 下载试卷的缓存文件夹目录
     * files
     *     -userId ：用户名Id
     *       -taskId ：任务Id
     *         -zip :存放当前taskId的试卷、学生 作答、老师批阅的信息
     *         -unzip ：解压的，目录名对应zip
     */
    fun downloadTestPaper(taskId: String, noteBookId: String) {

//        einkHomeworkView.setCurrentMode(currentMode)
//        einkHomeworkView.addBitmap(BitmapFactory.decodeFile(item.exampaperImagePath),BitmapFactory.decodeFile(item.studentAnswerImagePath),BitmapFactory.decodeFile(item.teacherCorrectImagePath),822,1200,1.1435f)

        var ss = Observable.create<String> {
                EinkFileUtils.saveRawToDataPath(BaseApplication.instance, R.raw.taskid, EinkFileUtils.getZipFileAbsolutePath(taskId, "712424997901045760.zip"))
                EinkFileUtils.saveRawToDataPath(BaseApplication.instance, R.raw.notebooks, EinkFileUtils.getZipFileAbsolutePath(taskId, "notebooks.zip"))
                it.onNext("1")
        }
                .map {
            //解压试卷
            ZipUtils.unzipFile(EinkFileUtils.getZipFileAbsolutePath(taskId, "712424997901045760.zip"), EinkFileUtils.getUnZipFileAbsolutePath("712424997901045760", ""))
            //解压学生作答和老师批阅
            ZipUtils.unzipFile(EinkFileUtils.getZipFileAbsolutePath(taskId, "notebooks.zip"), EinkFileUtils.getUnZipFileAbsolutePath("712424997901045760", ""))
            return@map ""
        }
                .map {
            //试卷的meta信息
            var metaStr = FileIOUtils.readFile2String(EinkFileUtils.getPath(taskId, "712424997901045760/meta.dat", "unzip"))
            val metaObject = JSON.parseObject(metaStr, MetaBean::class.java)
            //notebook信息
            var notebookStr = FileIOUtils.readFile2String(EinkFileUtils.getPath(taskId, "notebooks/712425681283190784/notebook.dat", "unzip"))
            val noteBookObject = JSON.parseObject(notebookStr, NoteBookBean::class.java)

            val einkHomeworkViewBean = EinkHomeworkViewBean()
            einkHomeworkViewBean.totalScore = metaObject.taskInfo.score.toString()
            einkHomeworkViewBean.totalPageNum = metaObject.answerPaper.answerSheetPages.size
            var paperListBeans = arrayListOf<EinkHomeworkViewBean.PaperListBean>()
            einkHomeworkViewBean.paperList = paperListBeans
            for (index in metaObject.answerPaper.answerSheetPages.indices) {
                var paperListBean = EinkHomeworkViewBean.PaperListBean()
                paperListBean.pageIndex = index + 1
                paperListBean.exampaperImagePath = EinkFileUtils.getPath(taskId, "712424997901045760/${metaObject.answerPaper.answerSheetPages[index]}", "unzip")

                var configureStrPath = EinkFileUtils.getPath(taskId, "notebooks/${noteBookObject.notepages[index].notebookId}/pages/${noteBookObject.notepages[index].notepageId}/configure.dat", "unzip")
                var configureStr = FileIOUtils.readFile2String(configureStrPath)
                var configureObject = JSON.parseObject(configureStr, PageConfigure::class.java)

                if (configureObject.layers.size == 1) {
                    paperListBean.studentAnswerImagePath = EinkFileUtils.getPath(taskId, "notebooks/${noteBookObject.notepages[index].notebookId}/pages/${noteBookObject.notepages[index].notepageId}//${configureObject.layers[0].layerName}/note.png", "unzip")
                } else if (configureObject.layers.size == 2) {
                    paperListBean.studentAnswerImagePath = EinkFileUtils.getPath(taskId, "notebooks/${noteBookObject.notepages[index].notebookId}/pages/${noteBookObject.notepages[index].notepageId}//${configureObject.layers[0].layerName}/note.png", "unzip")
                    paperListBean.teacherCorrectImagePath = EinkFileUtils.getPath(taskId, "notebooks/${noteBookObject.notepages[index].notebookId}/pages/${noteBookObject.notepages[index].notepageId}//${configureObject.layers[1].layerName}/note.png", "unzip")
                }
                paperListBeans.add(paperListBean)
            }
            return@map paperListBeans
        }
                .compose(RxSchedulers.compose())
                .subscribe {
            mView?.showCurrentStudent(it)
        }
    }
}
