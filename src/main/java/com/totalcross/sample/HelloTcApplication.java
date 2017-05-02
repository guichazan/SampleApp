package com.totalcross.sample;

import totalcross.TotalCrossApplication;

import com.totalcross.sample.ui.HelloTC;

public class HelloTcApplication {
	public static void main(String[] args) {
		TotalCrossApplication.run(HelloTC.class, "/r", "ACTIVATION_KEY",
				"/scr", "android",
				"/fontsize", "20",
				"/fingertouch");
	}
}
