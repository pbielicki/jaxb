package com.sun.xml.bind.v2.model.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;

import com.sun.xml.bind.api.impl.NameConverter;
import com.sun.xml.bind.v2.model.annotation.RuntimeInlineAnnotationReader;
import com.sun.xml.bind.v2.model.core.PropertyKind;
import com.sun.xml.bind.v2.model.core.TypeInfo;

/**
 * Unit Test for abstract PropertyInfoImpl class.
 * 
 * @author Przemyslaw Bielicki
 */
@SuppressWarnings("rawtypes")
public class PropertyInfoImplTest {

  List<PropertyInfoImpl<Type, Class, Field, Method>> propertyInfoImplList;
  List<String> propertyList;
  
  @Before
  public void before() {
    ModelBuilder<Type, Class, Field, Method> builder = 
        new RuntimeModelBuilder(null, new RuntimeInlineAnnotationReader(), new HashMap<Class, Class>(), "");
    
    ClassInfoImpl<Type, Class, Field, Method> parent = new ClassInfoImpl<Type, Class, Field, Method>(builder, null, TestClass.class);
    List<Method> getter = new ArrayList<Method>();
    List<Method> setter = new ArrayList<Method>();
    propertyList = new ArrayList<String>();
    for (Method m : TestClass.class.getDeclaredMethods()) {
      if (m.getName().startsWith("get")) {
        getter.add(m);
        propertyList.add(m.getName().substring(3));
      } else if (m.getName().startsWith("set")) {
        setter.add(m);
      }
    }
    assertEquals("Different number of setters and getters!", getter.size(), setter.size());
    
    PropertyInfoImpl.propertyDefaultLocalNameVariant = LocalNameVariant.CLASS_NAME;
    propertyInfoImplList = new ArrayList<PropertyInfoImpl<Type,Class,Field,Method>>();
    for (int i = 0; i < getter.size(); i++) {
      propertyInfoImplList.add(new TestPropertyInfoImpl(parent, 
        new GetterSetterPropertySeed<Type, Class, Field, Method>(parent, getter.get(i), setter.get(i))));
    }
  }

  @Test
  public void testCalcXmlNameXmlElement() {
    for (int i = 0; i < propertyList.size(); i++) {
      XmlElement xmlElement = mock(XmlElement.class);
      when(xmlElement.namespace()).thenReturn("namespace");
      when(xmlElement.name()).thenReturn("property", "", "##default");
      QName result = propertyInfoImplList.get(i).calcXmlName(xmlElement);
      assertEquals("property", result.getLocalPart());
      result = propertyInfoImplList.get(i).calcXmlName(xmlElement);
      assertEquals(propertyList.get(i), result.getLocalPart());
      result = propertyInfoImplList.get(i).calcXmlName(xmlElement);
      assertEquals(propertyList.get(i), result.getLocalPart());
    }
  }
  
  @Test
  public void testCalcXmlName() {
    for (int i = 0; i < propertyList.size(); i++) {
      QName result = propertyInfoImplList.get(i).calcXmlName((XmlElement) null);
      assertEquals(propertyList.get(i), result.getLocalPart());
    }
    
    PropertyInfoImpl.propertyDefaultLocalNameVariant = LocalNameVariant.CONSTANT_NAME;
    for (int i = 0; i < propertyList.size(); i++) {
      QName result = propertyInfoImplList.get(i).calcXmlName((XmlElement) null);
      assertEquals(NameConverter.standard.toConstantName(propertyList.get(i)), result.getLocalPart());
    }
    
    PropertyInfoImpl.propertyDefaultLocalNameVariant = LocalNameVariant.LOWER_CASE;
    for (int i = 0; i < propertyList.size(); i++) {
      QName result = propertyInfoImplList.get(i).calcXmlName((XmlElement) null);
      assertEquals(propertyList.get(i).toLowerCase(), result.getLocalPart());
    }
    
    PropertyInfoImpl.propertyDefaultLocalNameVariant = LocalNameVariant.MIXED_CLASS_NAME;
    for (int i = 0; i < propertyList.size(); i++) {
      QName result = propertyInfoImplList.get(i).calcXmlName((XmlElement) null);
      assertEquals(NameConverter.standard.toVariableName(propertyList.get(i)), result.getLocalPart());
    }
    
    PropertyInfoImpl.propertyDefaultLocalNameVariant = LocalNameVariant.UNCHANGED;
    for (int i = 0; i < propertyList.size(); i++) {
      QName result = propertyInfoImplList.get(i).calcXmlName((XmlElement) null);
      assertEquals(Introspector.decapitalize(propertyList.get(i)), result.getLocalPart());
    }
    
    PropertyInfoImpl.propertyDefaultLocalNameVariant = LocalNameVariant.UPPER_CASE;
    for (int i = 0; i < propertyList.size(); i++) {
      QName result = propertyInfoImplList.get(i).calcXmlName((XmlElement) null);
      assertEquals(propertyList.get(i).toUpperCase(), result.getLocalPart());
    }
  }

  /**
   * Auxiliary test classes.
   */
  static class TestPropertyInfoImpl extends PropertyInfoImpl<Type, Class, Field, Method> {
    public TestPropertyInfoImpl(ClassInfoImpl<Type, Class, Field, Method> parent, PropertySeed<Type, Class, Field, Method> spi) {
      super(parent, spi);
    }
    
    @Override
    public Collection<? extends TypeInfo<Type, Class>> ref() {
      return null;
    }

    @Override
    public PropertyKind kind() {
      return null;
    }
  }
  
  @XmlRootElement
  static class TestClass {
    String someValue;
    OtherClass otherClass;
    
    public void setOtherClass(OtherClass other) {
      this.otherClass = other;
    }
    
    public OtherClass getOtherClass() {
      return otherClass;
    }
    
    public void setSomeValue(String value) {
      this.someValue = value;
    }
    
    public String getSomeValue() {
      return someValue;
    }
  }
  
  static class OtherClass {
  }
}
