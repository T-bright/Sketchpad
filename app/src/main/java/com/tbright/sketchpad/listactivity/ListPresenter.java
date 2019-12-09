package com.tbright.sketchpad.listactivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.ZipUtils;
import com.tbright.sketchpad.BaseApplication;
import com.tbright.sketchpad.R;
import com.tbright.sketchpad.listactivity.bean.EinkHomeworkViewBean;
import com.tbright.sketchpad.listactivity.bean.MetaBean;
import com.tbright.sketchpad.listactivity.bean.NoteBookBean;
import com.tbright.sketchpad.listactivity.bean.PageConfigure;
import com.tbright.sketchpad.utils.EinkFileUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class ListPresenter {
    /**
     * 下载试卷的缓存文件夹目录
     * files
     * -userId ：用户名Id
     * -taskId ：任务Id
     * -zip :存放当前taskId的试卷、学生 作答、老师批阅的信息
     * -unzip ：解压的，目录名对应zip
     */
    public void downloadTestPaper(final String taskId, String noteBookId, final ResultListener resultListener) {

//        einkHomeworkView.setCurrentMode(currentMode)
//        einkHomeworkView.addBitmap(BitmapFactory.decodeFile(item.exampaperImagePath),BitmapFactory.decodeFile(item.studentAnswerImagePath),BitmapFactory.decodeFile(item.teacherCorrectImagePath),822,1200,1.1435f)
        Disposable ss = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                EinkFileUtils.saveRawToDataPath(BaseApplication.instance,
                        R.raw.taskid, EinkFileUtils.getZipFileAbsolutePath(taskId, "712424997901045760.zip"));
                EinkFileUtils.saveRawToDataPath(BaseApplication.instance,
                        R.raw.notebooks, EinkFileUtils.getZipFileAbsolutePath(taskId, "notebooks.zip"));
                e.onNext("");
            }
        }).map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                //解压试卷
                ZipUtils.unzipFile(EinkFileUtils.getZipFileAbsolutePath(taskId, "712424997901045760.zip"), EinkFileUtils.getUnZipFileAbsolutePath("712424997901045760", ""));
                //解压学生作答和老师批阅
                ZipUtils.unzipFile(EinkFileUtils.getZipFileAbsolutePath(taskId, "notebooks.zip"), EinkFileUtils.getUnZipFileAbsolutePath("712424997901045760", ""));
                return "";
            }
        }).map(new Function<String,ArrayList<EinkHomeworkViewBean.PaperListBean>>() {
            @Override
            public ArrayList<EinkHomeworkViewBean.PaperListBean> apply(String s) throws Exception {
                //试卷的meta信息
                String metaStr = FileIOUtils.readFile2String(EinkFileUtils.getPath(taskId, "712424997901045760/meta.dat", "unzip"));
                MetaBean metaObject = JSON.parseObject(metaStr, MetaBean.class);
                //notebook信息
                String notebookStr = FileIOUtils.readFile2String(EinkFileUtils.getPath(taskId, "notebooks/712425681283190784/notebook.dat", "unzip"));
                NoteBookBean noteBookObject = JSON.parseObject(notebookStr, NoteBookBean.class);

                EinkHomeworkViewBean einkHomeworkViewBean = new EinkHomeworkViewBean();
                einkHomeworkViewBean.setTotalScore(String.valueOf(metaObject.getTaskInfo().getScore()));
                einkHomeworkViewBean.setTotalPageNum(metaObject.getAnswerPaper().getAnswerSheetPages().size());
                ArrayList paperListBeans = new ArrayList<EinkHomeworkViewBean.PaperListBean>();
                einkHomeworkViewBean.setPaperList(paperListBeans);

                for (int index = 0; index < metaObject.getAnswerPaper().getAnswerSheetPages().size(); index++) {
                    EinkHomeworkViewBean.PaperListBean paperListBean = new EinkHomeworkViewBean.PaperListBean();
                    paperListBean.setPageIndex(index + 1);
                    paperListBean.setExampaperImagePath(EinkFileUtils.getPath(taskId, "712424997901045760/"+metaObject.getAnswerPaper().getAnswerSheetPages().get(index), "unzip"));

                    String configureStrPath = EinkFileUtils.getPath(taskId, "notebooks/"+noteBookObject.getNotepages().get(index).getNotebookId()+"/pages/"+noteBookObject.getNotepages().get(index).getNotepageId()+"/configure.dat", "unzip");
                    String configureStr = FileIOUtils.readFile2String(configureStrPath);
                    PageConfigure configureObject = JSON.parseObject(configureStr, PageConfigure.class);

                    List<EinkHomeworkViewBean.PaperListBean.InputRectsBean> inputRects = JSON.parseArray(inputsRectStr, EinkHomeworkViewBean.PaperListBean.InputRectsBean.class);
                    for(int i = 0;i< inputRects.size() ;i++){
                        EinkHomeworkViewBean.PaperListBean.InputRectsBean inputRect = inputRects.get(i);
//                            inputRect.
                    }
                    paperListBean.setInputRects(inputRects);
                    if (configureObject.getLayers().size() == 1) {
                        paperListBean.setStudentAnswerImagePath(
                                EinkFileUtils.getPath(taskId,
                                        "notebooks/"+noteBookObject.getNotepages().get(index).getNotebookId()+"/pages/"+noteBookObject.getNotepages().get(index).getNotepageId()+"/"+configureObject.getLayers().get(0).getLayerName()+"/note.png","unzip"));
                        paperListBean.setTeacherCorrectBitmap(BitmapFactory.decodeResource(BaseApplication.instance.getResources(), R.mipmap.aab));
                    } else if (configureObject.getLayers().size() == 2) {
                        paperListBean.setStudentAnswerImagePath(EinkFileUtils.getPath(taskId,
                                "notebooks/"+noteBookObject.getNotepages().get(index).getNotebookId()+"/pages/"+noteBookObject.getNotepages().get(index).getNotepageId()+"/"+configureObject.getLayers().get(0).getLayerName()+"/note.png", "unzip"));
                        String bt = EinkFileUtils.getPath(taskId,
                                "notebooks/"+noteBookObject.getNotepages().get(index).getNotebookId()+"/pages/"+noteBookObject.getNotepages().get(index).getNotepageId()+"/"+configureObject.getLayers().get(1).getLayerName()+"/note.png", "unzip");
                        paperListBean.setTeacherCorrectImagePath(bt);
                        paperListBean.setTeacherCorrectBitmap(BitmapFactory.decodeFile(bt));
                    }
                    paperListBeans.add(paperListBean);
                }
                return paperListBeans;
            }
        }).subscribe(new Consumer<ArrayList<EinkHomeworkViewBean.PaperListBean>>() {
            @Override
            public void accept(ArrayList<EinkHomeworkViewBean.PaperListBean> s) throws Exception {
                resultListener.result(s);
            }
        });
    }
    public interface ResultListener{
        void result(ArrayList<EinkHomeworkViewBean.PaperListBean> result);
    }
    private String inputsRectStr = "[\n" +
            "    {\n" +
            "        \"examId\": \"708533208924295168\",\n" +
            "        \"examInputId\": \"708533209113038848\",\n" +
            "        \"exampaperId\": \"708532379576176640\",\n" +
            "        \"exampaperRectId\": \"708533209129816064\",\n" +
            "        \"height\": 100,\n" +
            "        \"left\": 400,\n" +
            "        \"pageIndex\": 1,\n" +
            "        \"parentRectId\": 0,\n" +
            "        \"rectContent\": \"\",\n" +
            "        \"rectSequence\": 0,\n" +
            "        \"rectStyle\": \"\",\n" +
            "        \"rectStyleData\": \"\",\n" +
            "        \"rectType\": 6,\n" +
            "        \"top\": 200,\n" +
            "        \"width\": 200\n" +
            "    },\n" +
            "    {\n" +
            "        \"examId\": \"708533208924295168\",\n" +
            "        \"examInputId\": \"708533209113038848\",\n" +
            "        \"exampaperId\": \"708532379576176640\",\n" +
            "        \"exampaperRectId\": \"708533209159176192\",\n" +
            "        \"height\": 100,\n" +
            "        \"left\": 700,\n" +
            "        \"pageIndex\": 1,\n" +
            "        \"parentRectId\": 0,\n" +
            "        \"rectContent\": \"\",\n" +
            "        \"rectSequence\": 0,\n" +
            "        \"rectStyle\": \"\",\n" +
            "        \"rectStyleData\": \"\",\n" +
            "        \"rectType\": 7,\n" +
            "        \"top\": 200,\n" +
            "        \"width\": 200\n" +
            "    },\n" +
            "    {\n" +
            "        \"examId\": \"708533208924295168\",\n" +
            "        \"examInputId\": \"708533209113038848\",\n" +
            "        \"exampaperId\": \"708532379576176640\",\n" +
            "        \"exampaperRectId\": \"708533209129816064\",\n" +
            "        \"height\": 100,\n" +
            "        \"left\": 400,\n" +
            "        \"pageIndex\": 1,\n" +
            "        \"parentRectId\": 0,\n" +
            "        \"rectContent\": \"\",\n" +
            "        \"rectSequence\": 0,\n" +
            "        \"rectStyle\": \"\",\n" +
            "        \"rectStyleData\": \"\",\n" +
            "        \"rectType\": 6,\n" +
            "        \"top\": 400,\n" +
            "        \"width\": 200\n" +
            "    },\n" +
            "    {\n" +
            "        \"examId\": \"708533208924295168\",\n" +
            "        \"examInputId\": \"708533209113038848\",\n" +
            "        \"exampaperId\": \"708532379576176640\",\n" +
            "        \"exampaperRectId\": \"708533209159176192\",\n" +
            "        \"height\": 100,\n" +
            "        \"left\": 700,\n" +
            "        \"pageIndex\": 1,\n" +
            "        \"parentRectId\": 0,\n" +
            "        \"rectContent\": \"\",\n" +
            "        \"rectSequence\": 0,\n" +
            "        \"rectStyle\": \"\",\n" +
            "        \"rectStyleData\": \"\",\n" +
            "        \"rectType\": 7,\n" +
            "        \"top\": 400,\n" +
            "        \"width\": 200\n" +
            "    },\n" +
            "    {\n" +
            "        \"examId\": \"708533208924295168\",\n" +
            "        \"examInputId\": \"708533209113038848\",\n" +
            "        \"exampaperId\": \"708532379576176640\",\n" +
            "        \"exampaperRectId\": \"708533209129816064\",\n" +
            "        \"height\": 100,\n" +
            "        \"left\": 400,\n" +
            "        \"pageIndex\": 1,\n" +
            "        \"parentRectId\": 0,\n" +
            "        \"rectContent\": \"\",\n" +
            "        \"rectSequence\": 0,\n" +
            "        \"rectStyle\": \"\",\n" +
            "        \"rectStyleData\": \"\",\n" +
            "        \"rectType\": 6,\n" +
            "        \"top\": 600,\n" +
            "        \"width\": 200\n" +
            "    },\n" +
            "    {\n" +
            "        \"examId\": \"708533208924295168\",\n" +
            "        \"examInputId\": \"708533209113038848\",\n" +
            "        \"exampaperId\": \"708532379576176640\",\n" +
            "        \"exampaperRectId\": \"708533209159176192\",\n" +
            "        \"height\": 100,\n" +
            "        \"left\": 700,\n" +
            "        \"pageIndex\": 1,\n" +
            "        \"parentRectId\": 0,\n" +
            "        \"rectContent\": \"\",\n" +
            "        \"rectSequence\": 0,\n" +
            "        \"rectStyle\": \"\",\n" +
            "        \"rectStyleData\": \"\",\n" +
            "        \"rectType\": 7,\n" +
            "        \"top\": 600,\n" +
            "        \"width\": 200\n" +
            "    },\n" +
            "    {\n" +
            "        \"examId\": \"708533208924295168\",\n" +
            "        \"examInputId\": \"708533209113038848\",\n" +
            "        \"exampaperId\": \"708532379576176640\",\n" +
            "        \"exampaperRectId\": \"708533209129816064\",\n" +
            "        \"height\": 100,\n" +
            "        \"left\": 400,\n" +
            "        \"pageIndex\": 1,\n" +
            "        \"parentRectId\": 0,\n" +
            "        \"rectContent\": \"\",\n" +
            "        \"rectSequence\": 0,\n" +
            "        \"rectStyle\": \"\",\n" +
            "        \"rectStyleData\": \"\",\n" +
            "        \"rectType\": 6,\n" +
            "        \"top\": 800,\n" +
            "        \"width\": 200\n" +
            "    },\n" +
            "    {\n" +
            "        \"examId\": \"708533208924295168\",\n" +
            "        \"examInputId\": \"708533209113038848\",\n" +
            "        \"exampaperId\": \"708532379576176640\",\n" +
            "        \"exampaperRectId\": \"708533209159176192\",\n" +
            "        \"height\": 100,\n" +
            "        \"left\": 700,\n" +
            "        \"pageIndex\": 1,\n" +
            "        \"parentRectId\": 0,\n" +
            "        \"rectContent\": \"\",\n" +
            "        \"rectSequence\": 0,\n" +
            "        \"rectStyle\": \"\",\n" +
            "        \"rectStyleData\": \"\",\n" +
            "        \"rectType\": 7,\n" +
            "        \"top\": 800,\n" +
            "        \"width\": 200\n" +
            "    },\n" +
            "    {\n" +
            "        \"examId\": \"708533208924295168\",\n" +
            "        \"examInputId\": \"708533209113038848\",\n" +
            "        \"exampaperId\": \"708532379576176640\",\n" +
            "        \"exampaperRectId\": \"708533209129816064\",\n" +
            "        \"height\": 100,\n" +
            "        \"left\": 400,\n" +
            "        \"pageIndex\": 1,\n" +
            "        \"parentRectId\": 0,\n" +
            "        \"rectContent\": \"\",\n" +
            "        \"rectSequence\": 0,\n" +
            "        \"rectStyle\": \"\",\n" +
            "        \"rectStyleData\": \"\",\n" +
            "        \"rectType\": 6,\n" +
            "        \"top\": 1000,\n" +
            "        \"width\": 200\n" +
            "    },\n" +
            "    {\n" +
            "        \"examId\": \"708533208924295168\",\n" +
            "        \"examInputId\": \"708533209113038848\",\n" +
            "        \"exampaperId\": \"708532379576176640\",\n" +
            "        \"exampaperRectId\": \"708533209159176192\",\n" +
            "        \"height\": 100,\n" +
            "        \"left\": 700,\n" +
            "        \"pageIndex\": 1,\n" +
            "        \"parentRectId\": 0,\n" +
            "        \"rectContent\": \"\",\n" +
            "        \"rectSequence\": 0,\n" +
            "        \"rectStyle\": \"\",\n" +
            "        \"rectStyleData\": \"\",\n" +
            "        \"rectType\": 7,\n" +
            "        \"top\": 1000,\n" +
            "        \"width\": 200\n" +
            "    }\n" +
            "]";


}
