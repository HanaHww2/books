package com.book.data_set;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class SqlFileWriter {

  private SqlFileWriter() {}

  public static void writeLines(File out, List<String> lines) throws Exception {
    Files.createDirectories(out.toPath().getParent());

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(out, StandardCharsets.UTF_8, false))) {
      for (String line : lines) {
        if (line != null && !line.isBlank()) {
          bw.write(line);
        }
      }
    }
  }
}
