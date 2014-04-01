package com.sun.xml.bind.v2.model.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.junit.Test;

public class RuntimeInlineAnnotationReaderTest {
  
  RuntimeInlineAnnotationReader reader = new RuntimeInlineAnnotationReader();

  @Test
  public void testHasClassAnnotation() {
    assertTrue(reader.hasClassAnnotation(Direct.class, XmlRootElement.class));
    assertTrue(reader.hasClassAnnotation(Direct.class, XmlType.class));

    assertTrue(reader.hasClassAnnotation(RootBean.class, XmlRootElement.class));
    assertFalse(reader.hasClassAnnotation(RootBean.class, XmlType.class));
    
    assertTrue(reader.hasClassAnnotation(TypeBean.class, XmlType.class));
    assertFalse(reader.hasClassAnnotation(TypeBean.class, XmlRootElement.class));

    assertTrue(reader.hasClassAnnotation(FullBean.class, XmlRootElement.class));
    assertTrue(reader.hasClassAnnotation(FullBean.class, XmlType.class));
    
    assertFalse(reader.hasClassAnnotation(InvalidBean.class, XmlRootElement.class));
    assertFalse(reader.hasClassAnnotation(InvalidBean.class, XmlType.class));
  }
  
  @Test
  public void testOverrideAnnotation() {
    XmlRootElement a = reader.getAnnotation(XmlRootElement.class, Override.class.getAnnotations());
    assertEquals("XOverrideX", a.name());
  }
  
  @Test
  public void testNoOverrideAnnotation() {
    XmlRootElement a = reader.getAnnotation(XmlRootElement.class, Override1.class.getAnnotations());
    assertEquals("YOverrideY", a.name());
  }

  @XmlRootElement
  @XmlType
  static final class Direct {}

  @Root
  static final class RootBean {}

  @Type
  static final class TypeBean {}

  @Bean
  static final class FullBean {}

  @XmlRootElement(name = "XOverrideX")
  @Bean
  static final class Override {}

  @Bean
  @XmlRootElement(name = "YOverrideY")
  static final class Override1 {}
  
  @MetaMeta
  static final class InvalidBean {}
  
  @XmlRootElement
  @Retention(RetentionPolicy.RUNTIME)
  static @interface Root {}

  @XmlType
  @Retention(RetentionPolicy.RUNTIME)
  static @interface Type {}

  @XmlRootElement
  @XmlType
  @Retention(RetentionPolicy.RUNTIME)
  static @interface Bean {}
  
  @Bean
  @Retention(RetentionPolicy.RUNTIME)
  static @interface MetaMeta {}
}
