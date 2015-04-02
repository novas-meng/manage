package com.example.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.tsbx.Tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageCompress {        
	
	public static Bitmap compressImage(Bitmap image) {  
		  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        int options = 70;  
        while ( baos.toByteArray().length / 1024>100) {         
            baos.reset();  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
       // Bitmap bitmap=Tools.decodeBitmap(path)
        return bitmap;  
    }  
	public static Bitmap getimage(String srcPath) {  
        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        newOpts.inJustDecodeBounds = true;  
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);
          
        newOpts.inJustDecodeBounds = false;  
        int w = newOpts.outWidth;  
        int h = newOpts.outHeight;  
        float hh = 800f;
        float ww = 480f; 
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);  
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);  
        }  
        if (be <= 0)  
            be = 1;  
        newOpts.inSampleSize = be;
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
        return compressImage(bitmap); 
    }
	public static void saveMyBitmap(String bitName,Bitmap mBitmap,String path) throws IOException {  
	    File f = new File(path + bitName + ".png");  
	    f.createNewFile();  
	    FileOutputStream fOut = null;  
	    try {  
	            fOut = new FileOutputStream(f);  
	    } catch (FileNotFoundException e) {  
	            e.printStackTrace();  
	    }  
	    mBitmap= compressImage(mBitmap);
	    mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);  
	    
	    try {  
	            fOut.flush();  
	    } catch (IOException e) {  
	            e.printStackTrace();  
	    }  
	    try {  
	            fOut.close();  
	    } catch (IOException e) {  
	            e.printStackTrace();  
	    }  
	}  
	private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }
	public static void cleanCustomCache(String filePath) {   
        deleteFilesByDirectory(new File(filePath));
    }
}
