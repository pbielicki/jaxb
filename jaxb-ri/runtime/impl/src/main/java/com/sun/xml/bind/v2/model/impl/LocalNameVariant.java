package com.sun.xml.bind.v2.model.impl;

import com.sun.xml.bind.api.impl.NameConverter;

enum LocalNameVariant {
  CLASS_NAME {
    String convert(String name) {
      return NameConverter.standard.toClassName(name);
    }
  },
  CONSTANT_NAME {
    String convert(String name) {
      return NameConverter.standard.toConstantName(name);
    }
  },
  UNCHANGED,
  LOWER_CASE {
    String convert(String name) {
      return name.toLowerCase();
    }
  },
  MIXED_CLASS_NAME {
    String convert(String name) {
      return NameConverter.standard.toVariableName(name);
    }
  },
  UPPER_CASE {
    String convert(String name) {
      return name.toUpperCase();
    }
  };
  
  String convert(String name) {
    return name;
  }
}