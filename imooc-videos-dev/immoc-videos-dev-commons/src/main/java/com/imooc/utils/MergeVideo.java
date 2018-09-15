package com.imooc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author 2016wlw2 徐塬峰
 * 创建时间：2018年7月4日 下午8:46:47
 */
public class MergeVideo {

	private String ffmpegEXE;

	public MergeVideo(String ffmpegEXE) {
		super();
		this.ffmpegEXE = ffmpegEXE;
	}

	public void convertor(String mp3InputPath,String videoPath,double seconds, String outPath) throws IOException {
		// ffmpeg.exe -i test.mp4 spring.avi
//		ffmpeg.exe  -i bgm.mp3 -i video.mp4 -t 6 -y xu.mp4
		/*System.out.println(mp3InputPath);
		System.out.println(videoPath);
		System.out.println(seconds);
		System.out.println(outPath);
		*/
		List<String> command = new ArrayList<>();
		
		command.add(ffmpegEXE);
		
		
		command.add("-i");
		command.add(mp3InputPath);//mp3
		command.add("-i");
		command.add(videoPath);//video
		
		
		
		command.add("-t");
		command.add(String.valueOf(seconds));

		command.add("-y");
		command.add(outPath);//输出
		
		
		for (String c : command) {
			System.out.println(c);
		}
		ProcessBuilder builder = new ProcessBuilder(command);
		Process process = builder.start();

		InputStream errorStream = process.getErrorStream();
		InputStreamReader reader = new InputStreamReader(errorStream);
		BufferedReader br = new BufferedReader(reader);
		String line = "";
		while ((line = br.readLine()) != null) {

		}
		if (br != null) {
			br.close();
		}
		if (reader != null) {
			reader.close();
		}
		if (errorStream != null) {
			errorStream.close();
		}

	}

	public void convertor(String mp3InputPath, String outPath) throws IOException {
		// ffmpeg.exe -i test.mp4 spring.avi
//		ffmpeg.exe  -i bgm.mp3 -i video.mp4 -t 6 -y xu.mp4
		System.out.println(mp3InputPath);
		System.out.println(outPath);
		List<String> command = new ArrayList<>();
		command.add(ffmpegEXE);
		command.add("-i");
		command.add(mp3InputPath);//mp3
		command.add("-y");
		command.add(outPath);//输出
		
		
		for (String c : command) {
			System.out.println(c);
		}
		ProcessBuilder builder = new ProcessBuilder(command);
		Process process = builder.start();

		InputStream errorStream = process.getErrorStream();
		InputStreamReader reader = new InputStreamReader(errorStream);
		BufferedReader br = new BufferedReader(reader);
		String line = "";
		while ((line = br.readLine()) != null) {

		}
		if (br != null) {
			br.close();
		}
		if (reader != null) {
			reader.close();
		}
		if (errorStream != null) {
			errorStream.close();
		}

	}
	
	
	
	public static void main(String[] args) {

		MergeVideo ffmpeg = new MergeVideo("G:\\ffmepg视频处理方案\\ffmpeg-20180704-3b10bb8-win64-static\\bin\\ffmpeg.exe");
		try {
			ffmpeg.convertor("G:\\ffmepg视频处理方案\\ffmpeg-20180704-3b10bb8-win64-static\\bin\\bgm.mp3","G:\\ffmepg视频处理方案\\ffmpeg-20180704-3b10bb8-win64-static\\bin\\video.mp4", 7,"G:\\ffmepg视频处理方案\\ffmpeg-20180704-3b10bb8-win64-static\\bin\\xuyuanfeng.mp4");
		    System.out.println("转换成功");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
