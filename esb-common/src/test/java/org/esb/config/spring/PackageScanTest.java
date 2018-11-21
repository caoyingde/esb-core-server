package org.esb.config.spring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

public class PackageScanTest {

	private static final String RESOURCE_PATTERN = "/**/*.class";
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	
	public List<Class<?>> scanPackages(String... packagesToScan) {
		List<Class<?>> interfaceClasses = new ArrayList<Class<?>>();
		for(String pkg : packagesToScan){
			String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
					ClassUtils.convertClassNameToResourcePath(pkg) + RESOURCE_PATTERN;
			try {
				Resource[] resources = resourcePatternResolver.getResources(pattern);
				MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);
				for (Resource resource : resources) {
					if (resource.isReadable()) {
						MetadataReader reader = readerFactory.getMetadataReader(resource);
						String className = reader.getClassMetadata().getClassName();
						Class<?> clz = Class.forName(className);
						if(clz.isInterface()){
							interfaceClasses.add(clz);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return interfaceClasses;
	}
	
	public static void main(String[] args) {
		PackageScanTest packageScanTest = new PackageScanTest();
		List<Class<?>> clzs = packageScanTest.scanPackages("org.example");
		System.out.println(clzs.size());
	}
}
