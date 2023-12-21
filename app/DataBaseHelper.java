import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SQLiteUpDownService {
    private static SQLiteUpDownService instance;
    private static final String extDb3FilePath = "databases/siheung_database";
    private static final String innerDbPath = "data/data/패키지1/databases/siheung_database";

    private SQLiteUpDownService(){
        //기본 Constructor입니다.    }

        public static SQLiteUpDownService getInstance(){
            if(instance==null){
                instance = new SQLiteUpDownService();
            }
            return instance;
        }//싱글턴 방식을 사용합니다.
        //업로드 기능입니다.    //PDA to PC    public void upload(){

    }


        if(checkFile(context)){

        //extDb3File(외부 파일) 을 내부 경로에 복사합니다. (PC to local DB)            File extDb3File = new File(extDb3FilePath);

        File innerDb3File = new File(innerDbPath);
        if(innerDb3File.exists()==false){

            copyDataBase(context);
        }
    }

}

    private boolean checkFile(Context context){
        //파일이 존재할 경우 true를 리턴해주는 파일 존재 확인용 메소드입니다.
        try {

            InputStream myInput = context.getAssets().open(extDb3FilePath);
        } catch (IOException e) {
            return false;
        }
        return true;

    }

    public void copyDataBase(Context context){
        String PACKAGE_NAME = context.getPackageName();

        AssetManager manager = context.getAssets();
        String folderPath = "/data/data/" + PACKAGE_NAME + "/databases";
        String filePath = "/data/data/" + PACKAGE_NAME + "/databases/데이터베이스파일명";
        File folder = new File(folderPath);
        File file = new File(filePath);

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            InputStream is = manager.open("databases/데이터베이스파일명");
            BufferedInputStream bis = new BufferedInputStream(is);

            if (folder.exists()) {
            }else{
                folder.mkdirs();
            }

            if (file.exists()) {
                file.delete();
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            int read = -1;
            byte[] buffer = new byte[1024];
            while ((read = bis.read(buffer, 0, 1024)) != -1) {
                bos.write(buffer, 0, read);
            }

            bos.flush();

            bos.close();
            fos.close();
            bis.close();
            is.close();

        } catch (IOException e) {
            Log.e("ErrorMessage : ", e.getMessage());
        }
    }
}