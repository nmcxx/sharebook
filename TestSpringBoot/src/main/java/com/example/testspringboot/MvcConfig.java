package com.example.testspringboot;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // set resource static de su dung tep images
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
//		WebMvcConfigurer.super.addResourceHandlers(registry);
		Path imageUploadDir = Paths.get("./images");
		String imagePath = imageUploadDir.toFile().getAbsolutePath();
		
		Path pdfUploadDir = Paths.get("./pdf");
		String pdfPath = pdfUploadDir.toFile().getAbsolutePath();
		
		// cau hinh du lieu tinh pdf va img, cho phep anh xa url voi vi tri thuc te cua nguon du lieu
		registry.addResourceHandler("/images/**").addResourceLocations("file:/"+imagePath+"/");
		registry.addResourceHandler("/pdf/").addResourceLocations("file:/"+pdfPath+"/");
	}

	
}
