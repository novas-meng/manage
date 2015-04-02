package com.example.tools;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Map;

public class SocketHttpRequester   
{ 
	public static String request;
    public static boolean post(String path, Map<String, String> params, FormFile[] files) throws Exception  

    {     
        final String BOUNDARY = "---------------------------7da2137580612";   
        final String endline = "--" + BOUNDARY + "--\r\n";  
        
        //��������forѭ������Ϊ�˵õ����ݳ��Ȳ��������ݱ������Ͷ���  
        //���ȵõ��ļ��������ݵ��ܳ���(�����ļ��ָ���)  
        int fileDataLength = 0;  
        for(FormFile uploadFile : files)  
        {  
            StringBuilder fileExplain = new StringBuilder();  
            fileExplain.append("--");  
            fileExplain.append(BOUNDARY);  
            fileExplain.append("\r\n");  
            fileExplain.append("Content-Disposition: form-data;name=\""+ uploadFile.getParameterName()+"\";filename=\""+ uploadFile.getFilname() + "\"\r\n");  
            fileExplain.append("Content-Type: "+ uploadFile.getContentType()+"\r\n\r\n");  
            fileExplain.append("\r\n");  
            fileDataLength += fileExplain.length();  
            if(uploadFile.getInStream()!=null){  
                fileDataLength += uploadFile.getFile().length();  
           }else{  
                fileDataLength += uploadFile.getData().length;  
            }  
        }  
        //�ٹ����ı����Ͳ�����ʵ������  
        StringBuilder textEntity = new StringBuilder();          
        for (Map.Entry<String, String> entry : params.entrySet())   
        {    
            textEntity.append("--");  
            textEntity.append(BOUNDARY);  
            textEntity.append("\r\n");  
            textEntity.append("Content-Disposition: form-data; name=\""+ entry.getKey() + "\"\r\n\r\n");  
            textEntity.append(entry.getValue());  
            textEntity.append("\r\n");  
        }         
        //���㴫�����������ʵ�������ܳ���(�ı��ܳ���+�����ܳ���+�ָ���)  
        int dataLength = textEntity.toString().getBytes().length + fileDataLength +  endline.getBytes().length;         
        URL url = new URL(path);  
        //Ĭ�϶˿ں���ʵ���Բ�д  
        int port = url.getPort()==-1 ? 80 : url.getPort();  
        //����һ��Socket����  
        Socket socket = new Socket(InetAddress.getByName(url.getHost()), port);  
        //���һ�����������Android����web��  
        OutputStream outStream = socket.getOutputStream();  
        //�������HTTP����ͷ�ķ���  
        String requestmethod = "POST "+ url.getPath()+" HTTP/1.1\r\n"; 
        outStream.write(requestmethod.getBytes());  
        //����accept  
        String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*/r/n";  
        outStream.write(accept.getBytes());  
        //����language  
        String language = "Accept-Language: zh-CN\r\n";  
        outStream.write(language.getBytes());  
        //����contenttype  
        String contenttype = "Content-Type: multipart/form-data; boundary="+ BOUNDARY+ "\r\n";  
        outStream.write(contenttype.getBytes());  
        //����contentlength  
        String contentlength = "Content-Length: "+ dataLength + "\r\n";  
        outStream.write(contentlength.getBytes());  
        //����alive  
        String alive = "Connection: Keep-Alive\r\n";          
        outStream.write(alive.getBytes());  
        //����host  
        String host = "Host: "+ url.getHost() +":"+ port +"\r\n";  
        outStream.write(host.getBytes());  
        //д��HTTP����ͷ�����HTTPЭ����дһ���س�����  
        outStream.write("\r\n".getBytes());  
        //�������ı����͵�ʵ�����ݷ��ͳ���  
        outStream.write(textEntity.toString().getBytes());           
        //�������ļ����͵�ʵ�����ݷ��ͳ���  
        for(FormFile uploadFile : files)  
        {  
            StringBuilder fileEntity = new StringBuilder();  
            fileEntity.append("--");  
            fileEntity.append(BOUNDARY);  
            fileEntity.append("\r\n");  
            fileEntity.append("Content-Disposition: form-data;name=\""+ uploadFile.getParameterName()+"\";filename=\""+ uploadFile.getFilname() + "\"\r\n");  
            fileEntity.append("Content-Type: "+ uploadFile.getContentType()+"\r\n\r\n");  
            outStream.write(fileEntity.toString().getBytes());  
            //�߶���д  
            if(uploadFile.getInStream()!=null)  
            {  
                byte[] buffer = new byte[1024];  
                int len = 0;  
                while((len = uploadFile.getInStream().read(buffer, 0, 1024))!=-1)  
                {  
                    outStream.write(buffer, 0, len);  
                }  
                uploadFile.getInStream().close();  
            }  
            else  
            {  
                outStream.write(uploadFile.getData(), 0, uploadFile.getData().length);  
            }  
            outStream.write("\r\n".getBytes());  
        }  
        //���淢�����ݽ�����־����ʾ�����Ѿ�����  
        outStream.write(endline.getBytes());          
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
        //��ȡweb���������ص����ݣ��ж��������Ƿ�Ϊ200���������200����������ʧ��  
        if(reader.readLine().indexOf("200")==-1)  
        {  
        	request="fail";
            return false;  
        }  
        else request=reader.readLine();
        outStream.flush();  
        outStream.close();  
        reader.close();  
        socket.close();  
        return true;  
    }  
    public static boolean post(String path, Map<String, String> params, FormFile file) throws Exception  
    {  
       return post(path, params, new FormFile[]{file});  
    }  
    public static byte[] readStream(InputStream inStream) throws Exception  
    {  
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();  
        byte[] buffer = new byte[1024];  
        int len = -1;  
        while( (len=inStream.read(buffer)) != -1)  
        {  
            outSteam.write(buffer, 0, len);  
        }  
        outSteam.close();  
        inStream.close();  
        return outSteam.toByteArray();  
    }  
}
