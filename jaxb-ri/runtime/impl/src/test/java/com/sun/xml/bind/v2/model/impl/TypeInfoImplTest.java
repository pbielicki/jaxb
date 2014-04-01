package com.sun.xml.bind.v2.model.impl;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;

import com.sun.xml.bind.v2.model.annotation.Locatable;
import com.sun.xml.bind.v2.model.annotation.RuntimeInlineAnnotationReader;
import com.sun.xml.bind.v2.runtime.Location;

/**
 * Unit Test for abstract TypeInfoImpl class.
 * 
 * @author Przemyslaw Bielicki
 */
@SuppressWarnings("rawtypes")
public class TypeInfoImplTest {

  TypeInfoImpl<Type, Class, Field, Method> typeInfoImpl;

  @Before
  public void before() {
    ModelBuilder<Type, Class, Field, Method> builder = 
        new RuntimeModelBuilder(null, new RuntimeInlineAnnotationReader(), new HashMap<Class, Class>(), "");

    TypeInfoImpl.elementDefaultLocalNameVariant = LocalNameVariant.CLASS_NAME;
    TypeInfoImpl.typeDefaultLocalNameVariant = LocalNameVariant.CONSTANT_NAME;
    typeInfoImpl = new TestTypeInfoImpl(builder, null);
  }
  
  @Test
  public void testParseElementName() {
    QName result = typeInfoImpl.parseElementName(TestClass.class);
    assertEquals("TestClass", result.getLocalPart());

    TypeInfoImpl.elementDefaultLocalNameVariant = LocalNameVariant.CONSTANT_NAME;
    result = typeInfoImpl.parseElementName(TestClass.class);
    assertEquals("TEST_CLASS", result.getLocalPart());

    TypeInfoImpl.elementDefaultLocalNameVariant = LocalNameVariant.LOWER_CASE;
    result = typeInfoImpl.parseElementName(TestClass.class);
    assertEquals("testclass", result.getLocalPart());

    TypeInfoImpl.elementDefaultLocalNameVariant = LocalNameVariant.MIXED_CLASS_NAME;
    result = typeInfoImpl.parseElementName(TestClass.class);
    assertEquals("testClass", result.getLocalPart());

    TypeInfoImpl.elementDefaultLocalNameVariant = LocalNameVariant.UPPER_CASE;
    result = typeInfoImpl.parseElementName(TestClass.class);
    assertEquals("TESTCLASS", result.getLocalPart());

    result = typeInfoImpl.parseElementName(TestNamedClass.class);
    assertEquals("XTestNamedClassX", result.getLocalPart());
  }
  
  @Test
  public void testParseTypeName() {
    QName result = typeInfoImpl.parseTypeName(TestClass.class);
    assertEquals("TEST_CLASS", result.getLocalPart());

    TypeInfoImpl.typeDefaultLocalNameVariant = LocalNameVariant.CLASS_NAME;
    result = typeInfoImpl.parseTypeName(TestClass.class);
    assertEquals("TestClass", result.getLocalPart());

    TypeInfoImpl.typeDefaultLocalNameVariant = LocalNameVariant.LOWER_CASE;
    result = typeInfoImpl.parseTypeName(TestClass.class);
    assertEquals("testclass", result.getLocalPart());

    TypeInfoImpl.typeDefaultLocalNameVariant = LocalNameVariant.MIXED_CLASS_NAME;
    result = typeInfoImpl.parseTypeName(TestClass.class);
    assertEquals("testClass", result.getLocalPart());

    TypeInfoImpl.typeDefaultLocalNameVariant = LocalNameVariant.UPPER_CASE;
    result = typeInfoImpl.parseTypeName(TestClass.class);
    assertEquals("TESTCLASS", result.getLocalPart());

    result = typeInfoImpl.parseTypeName(TestNamedClass.class);
    assertEquals("YTestNamedClassY", result.getLocalPart());
  }

  /**
   * Auxiliary test classes.
   */
  static class TestTypeInfoImpl extends TypeInfoImpl<Type, Class, Field, Method> {
    public TestTypeInfoImpl(ModelBuilder<Type, Class, Field, Method> builder, Locatable upstream) {
      super(builder, upstream);
    }
    
    @Override
    public Type getType() {
      return null;
    }
    @Override
    public boolean canBeReferencedByIDREF() {
      return false;
    }
    @Override
    public Location getLocation() {
      return null;
    }
  }

  @XmlType
  @XmlRootElement
  static class TestClass {
  }
  
  @XmlType(name = "YTestNamedClassY")
  @XmlRootElement(name = "XTestNamedClassX")
  static class TestNamedClass {
  }
}
