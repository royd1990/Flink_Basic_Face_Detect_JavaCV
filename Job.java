package org.Improcessing.javacv;

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;

import org.apache.flink.util.Collector;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier;
import org.bytedeco.javacpp.opencv_core.RectVector;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_face;
import org.bytedeco.javacpp.opencv_imgcodecs.*;
import org.bytedeco.javacpp.opencv_imgcodecs;

import static org.bytedeco.javacpp.opencv_imgproc.*;

/**
 * Skeleton for a Flink Job.
 *
 * For a full example of a Flink Job, see the WordCountJob.java file in the
 * same package/directory or have a look at the website.
 *
 * You can also generate a .jar file that you can submit on your Flink
 * cluster.
 * Just type
 * 		mvn clean package
 * in the projects root directory.
 * You will find the jar in
 * 		target/flink-quickstart-0.1-SNAPSHOT-Sample.jar
 *
 */
public class Job {

	public static void main(String[] args) throws Exception {
		// set up the execution environment
		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		DataSet<ImageLoad> l = env.fromElements(new ImageLoad("/home/royd1990/Documents/Flink_Project/Improcessing/Messi.jpeg"));
		l.flatMap(new FaceRecognize()).print();

		/**
		 * Here, you can start creating your execution plan for Flink.
		 *
		 * Start with getting some data from the environment, like
		 * 	env.readTextFile(textPath);
		 *
		 * then, transform the resulting DataSet<String> using operations
		 * like
		 * 	.filter()
		 * 	.flatMap()
		 * 	.join()
		 * 	.coGroup()
		 * and many more.
		 * Have a look at the programming guide for the Java API:
		 *
		 * http://flink.apache.org/docs/latest/apis/batch/index.html
		 *
		 * and the examples
		 *
		 * http://flink.apache.org/docs/latest/apis/batch/examples.html
		 *
		 */

		// execute program

	}

	private static class FaceRecognize implements org.apache.flink.api.common.functions.FlatMapFunction<ImageLoad, Object> {
		public FaceRecognize(){

		}
		@Override
		public void flatMap(ImageLoad value, Collector<Object> out) throws Exception {
			int height = 100;
			int width = 75;
			CascadeClassifier facedetetor = new CascadeClassifier("lbpcascade_frontalface.xml");
			Mat image = value.getImage();
			Mat imageGray = new Mat();
			cvtColor(image,imageGray,COLOR_BGR2GRAY);
			equalizeHist(imageGray,imageGray);
			RectVector faces = new RectVector();
			facedetetor.detectMultiScale(imageGray,faces);
			for (int i = 0; i < faces.size(); i++) {
				Rect face_i = faces.get(i);
				Mat face = new Mat(imageGray, face_i);
				Mat face_resized = new Mat();
				resize(face, face_resized, new opencv_core.Size(width, height),
						1.0, 1.0, INTER_CUBIC);
				rectangle(imageGray, face_i, new opencv_core.Scalar(0, 255, 0, 1));


			}
			opencv_imgcodecs.imwrite("Messi_Face.jpeg",imageGray);
		}
	}
}
