package com.bluedatax.w65.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bluedatax.w65.BaseActivity;
import com.bluedatax.w65.R;
import com.bluedatax.w65.fragment.FirstPager;
import com.bluedatax.w65.utils.SharedPrefsUtil;
import com.bluedatax.w65.utils.downloadimage.HttpUtils;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 设置头像
 * Created by bdx108 on 15/11/25.
 */
public class SetIcon extends BaseActivity implements View.OnClickListener {
    private TextView mTextViewLeft;//标题左边内容
    private TextView mTextViewMiddle;//标题中间内容
    private RelativeLayout mLayoutTakePhoto;//拍照按钮
    private RelativeLayout mLayoutPhoto;//调用系统相册按钮
    private ImageView mImageViewImage;//显示头像的imageview
    private static final int PICTURE_FROM_CAMERA=101;
    private static final int PICTURE_FROM_GALLERY=102;


    private File file;
    private Uri uri;
    private String fub;
    private String auid;
    private boolean _isExe = false;
    private downloadImageTask task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seticon);
        initWidget();
        fub = SharedPrefsUtil.getValue(this, "fub", "");
        auid = SharedPrefsUtil.getValue(this,"auid","");
        String path=Environment.getExternalStorageDirectory()+"/w65/icon_bitmap/"+ "myicon.jpg";
        if (path!=null){
            mImageViewImage.setImageBitmap(new Setting().getDiskBitmap(path));
        }else {
            Toast.makeText(getApplicationContext(), "zanshi没有照片", Toast.LENGTH_LONG).show();
        }
        task = new downloadImageTask();
    }


    private void initWidget() {
        mTextViewLeft= (TextView) findViewById(R.id.textview_title_left);
        mTextViewMiddle= (TextView) findViewById(R.id.textview_title_middle);
        mImageViewImage= (ImageView) findViewById(R.id.circleimage_seticon);
        mImageViewImage.setOnClickListener(this);
        mLayoutTakePhoto= (RelativeLayout) findViewById(R.id.layout_takephoto);
        mLayoutTakePhoto.setOnClickListener(this);
        mLayoutPhoto = (RelativeLayout) findViewById(R.id.layout_photo);
        mLayoutPhoto.setOnClickListener(this);
        mTextViewLeft.setText("账户");
        mTextViewMiddle.setText("设置头像");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_takephoto:
                startCamera();
                break;
            case R.id.layout_photo:
                Intent intent = new Intent();
                //设置启动相册的Action
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //设置类型
                intent.setType("image/*");
                //启动相册，这里使用有返回结果的启动
                startActivityForResult(intent, PICTURE_FROM_GALLERY);
                break;
            case R.id.circleimage_seticon:
                Intent intent1=new Intent(SetIcon.this,ImageLarge.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    /**
     * 调用系统相机拍照
     */
    private void startCamera() {
        Intent intent = new Intent();
        //启动相机的Action
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        //文件的保存位置
        file = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
        //设置图片拍摄后保存的位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        //启动相机，这里使用有返回结果的启动
        startActivityForResult(intent, PICTURE_FROM_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICTURE_FROM_CAMERA:
                    Bitmap bp = getCameraBitmap();
                    try {
                        saveFile(bp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    upload();
                    mImageViewImage.setImageBitmap(bp);
                    break;
                case PICTURE_FROM_GALLERY:
                    Bitmap bit = getPhotoBitmap(data);
                    try {
                        saveFile(bit);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    upload();
                    mImageViewImage.setImageBitmap(bit);
                    break;
            }
        }
    }

    private void upload() {
        if (!_isExe) {
            task.execute(String.format("%s?prod=w65&id=1&fttype=ul_files&auth_user=w65_wdas&" +
                    "auth_name=usr_u&fname=profile_img&auth_id=%s&type=png&bdx=prod", fub, auid));
        }
        _isExe = true;
    }
    class downloadImageTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            System.out.println("[downloadImageTask->]doInBackground "
                    + params[0]);
            String response = HttpUtils.getResponse(params[0]);
            Log.i("返回的数据",response);
            return true;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            System.out.println("result = " + result);
            super.onPostExecute(result);
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }
    }

    /**
     *
     * 上传图片
     *
     */

    public Bitmap getPhotoBitmap(Intent data) {
        Bitmap bitmap=null;
        Bitmap bit=null;
        try {
            uri=data.getData();
            bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            bitmap= ThumbnailUtils.extractThumbnail(bitmap, 300, 300);
            bit=toRoundBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //return bit;
        return bitmap;
    }

    /**
     * 得到相机拍照后的照片的bitmap
     * @return
     */
    public Bitmap getCameraBitmap() {
        Bitmap bitmap=null;
        try {
            bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(file)));
            bitmap= ThumbnailUtils.extractThumbnail(bitmap, 300, 300);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //Bitmap pic = getBitmap();//另一种显示图片的方法
        //toRoundBitmap(pic);
        //返回一个圆形的bitmap
        //return toRoundBitmap(bitmap);
        return bitmap;
    }
//    private Bitmap getBitmap() {
//        BitmapFactory.Options op = new BitmapFactory.Options();
//        op.inSampleSize = 10;  //这个数字越大,图片大小越小.
//        Bitmap pic= BitmapFactory.decodeFile(file.getAbsolutePath(), op);
//        //这个ImageView是拍照完成后显示图片
//        FileOutputStream b = null;
//        try {
//            b = new FileOutputStream(file.getAbsolutePath());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        if(pic != null){
//            pic.compress(Bitmap.CompressFormat.JPEG, 100, b);
//        }
//        return pic;
//    }

    /**
     * 将一个bitmap转化为圆形输出
     * @param bitmap
     * @return
     */
    public Bitmap toRoundBitmap(Bitmap bitmap){
        int width=bitmap.getWidth();
        int height=bitmap.getHeight();
        int r=0;
        if (width<height){
            r=width;
        }else {
            r=height;
        }
        Bitmap backgroundBitmap=Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(backgroundBitmap);
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        RectF rectF=new RectF(0,0,r,r);
        canvas.drawRoundRect(rectF,r/2,r/2,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap,null,rectF,paint);
        return backgroundBitmap;
    }
    /**
     * 保存文件
     * @param bm
     * @throws IOException
     */
    public File saveFile(Bitmap bm) throws IOException {
        String path = Environment.getExternalStorageDirectory().toString()+"/w65/icon_bitmap/";
        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdirs();
        }
        File myIconFile= new File(path + "myicon.jpg");
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myIconFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return myIconFile;
    }
}
