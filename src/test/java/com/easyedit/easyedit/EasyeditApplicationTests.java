package com.easyedit.easyedit;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

@SpringBootTest
class EasyeditApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
    void CreateXML() {
		FastAutoGenerator.create("jdbc:mysql://0000000:3306/easeedit_test?useUnicode=true&characterEncoding=utf-8&useSSL=true", "", "")
			.globalConfig(builder -> {
				builder.author("zhongjun") // 设置作者
					.enableSwagger() // 开启 swagger 模式
					.outputDir("C://zhongjun//code//del//edit//"); // 指定输出目录
			})
			.packageConfig(builder -> {
				builder.parent("com.easyedit.easyedit") // 设置父包名
					.pathInfo(Collections.singletonMap(OutputFile.xml, "C://zhongjun//code//del//edit//")); // 设置mapperXml生成路径
			})
			.strategyConfig(builder -> {
				builder.addInclude("user"); // 设置需要生成的表名
					// .addInclude("pagetree"); 
			})
			.templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
			.execute();
	}

}
