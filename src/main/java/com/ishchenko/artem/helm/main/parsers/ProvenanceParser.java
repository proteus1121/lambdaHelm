package com.ishchenko.artem.helm.main.parsers;

import com.ishchenko.artem.helm.main.model.HelmAttributes;
import com.ishchenko.artem.helm.main.model.HelmProperties;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class ProvenanceParser
{
  public HelmAttributes parse(final InputStream inputStream) throws IOException {
    HelmAttributes attributes = new HelmAttributes();
    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        if (line.startsWith(HelmProperties.NAME.getPropertyName())) {
          attributes.setName(getValue(line));
        }
        if (line.startsWith(HelmProperties.DESCRIPTION.getPropertyName())) {
          attributes.setDescription(getValue(line));
        }
        if (line.startsWith(HelmProperties.VERSION.getPropertyName())) {
          attributes.setVersion(getValue(line));
        }
        if (line.startsWith(HelmProperties.ICON.getPropertyName())) {
          attributes.setIcon(getValue(line));
        }
        if (line.startsWith(HelmProperties.APP_VERSION.getPropertyName())) {
          attributes.setAppVersion(getValue(line));
        }
      }
    }
    return attributes;
  }

  private String getValue(String string) {
    return string.substring(string.indexOf(":") + 1).trim();
  }
}
