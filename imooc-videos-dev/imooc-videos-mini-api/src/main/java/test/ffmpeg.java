package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ffmpeg {

	private String ffmpegEXE;

	public ffmpeg(String ffmpegEXE) {
		super();
		this.ffmpegEXE = ffmpegEXE;
	}

	public void convertor(String videoPath, String outPath) throws IOException {
		// ffmpeg.exe -i test.mp4 spring.avi
		List<String> command = new ArrayList<>();
		command.add(ffmpegEXE);
		command.add("-i");
		command.add(videoPath);
		command.add(outPath);
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

		ffmpeg ffmpeg = new ffmpeg("G:\\ffmepg视频处理方案\\ffmpeg-20180704-3b10bb8-win64-static\\bin\\ffmpeg.exe");
		try {
			ffmpeg.convertor("G:\\ffmepg视频处理方案\\ffmpeg-20180704-3b10bb8-win64-static\\bin\\test.mp4",
					"G:\\ffmpegOut\\北京北京.avi");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
