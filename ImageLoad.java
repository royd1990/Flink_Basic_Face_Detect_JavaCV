package org.Improcessing.javacv;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_imgcodecs;

/**
 * Created by royd1990 on 06/06/16.
 */
public class ImageLoad {
    public Mat m;
    public String path;
    public ImageLoad(){
    }
    public ImageLoad(String path){
        this.path=path;
        m = opencv_imgcodecs.imread(path);
    }

    public Mat getImage(){return m;}


}
