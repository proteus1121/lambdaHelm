package com.ishchenko.artem.helm.main.parsers;/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2018-present Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Service for working with tgz files
 */
@Service
public class TgzParser
{
  private static final String CHART_NAME = "Chart.yaml";

  public InputStream getChartFromInputStream(final InputStream is) throws IOException {
    try (GzipCompressorInputStream gzis = new GzipCompressorInputStream(is)) {
      try (TarArchiveInputStream tais = new TarArchiveInputStream(gzis)) {
        ArchiveEntry currentEntry;
        while ((currentEntry = tais.getNextEntry()) != null) {
          if (currentEntry.getName().endsWith(CHART_NAME)) {
            byte[] buf = new byte[(int) currentEntry.getSize()];
            tais.read(buf, 0, buf.length);
            return new ByteArrayInputStream(buf);
          }
        }
      }
    }
    throw new IllegalArgumentException(String.format("%s not found", CHART_NAME));
  }
}
