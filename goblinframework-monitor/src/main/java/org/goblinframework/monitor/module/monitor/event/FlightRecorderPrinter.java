package org.goblinframework.monitor.module.monitor.event;

import org.goblinframework.api.monitor.Instruction;
import org.goblinframework.core.util.StringUtils;
import org.goblinframework.monitor.flight.Flight;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

abstract class FlightRecorderPrinter {

  public static String generatePrettyLog(Flight flight) {
    List<String> list = new ArrayList<>();
    list.add("[" + flight.startPoint() + "]");
    Object request = flight.location().attribute("request");
    if (request instanceof String) {
      list.addAll(generateRequest((String) request));
    }
    list.addAll(generateLocation(flight.location().id()));
    list.add(" |- duration:  " + flight.durationMillis() + "ms");
    list.add(" |- forceLog:  " + true);
    list.add(" \\- sequence:  " + flight.getInstructions().instructionCount());

    for (int i = 0; i < flight.getInstructions().size(); i++) {
      Instruction instruction = flight.getInstructions().get(i);

      String[] headers = getHeaders(i, flight, instruction);
      int lenPerLine = 100 - headers[0].length() - 1;

      List<String> L = new ArrayList<>();
      String line = instruction.translator().translate(true);
      int l = line.length();
      if (l <= lenPerLine) {
        L.add(line);
      } else {
        while (true) {
          L.add(line.substring(0, lenPerLine));
          line = line.substring(lenPerLine, line.length());
          l = line.length();
          if (l <= lenPerLine) {
            L.add(line);
            break;
          }
        }
      }
      List<String> LL = new ArrayList<>();
      for (int j = 0; j < L.size(); j++) {
        String s = L.get(j);
        if (j == 0) {
          s = headers[0] + s;
        } else {
          s = headers[1] + s;
        }
        LL.add(s);
      }

      for (int j = 0; j < Math.min(LL.size(), 15); j++) {
        list.add(LL.get(j));
      }
      if (LL.size() > 15) {
        list.add(headers[1] + "......");
      }
    }

    return StringUtils.join(list, "\n");
  }

  private static List<String> generateRequest(String location) {
    List<String> list = new LinkedList<>();
    String first = " |- request:   " + location;
    if (first.length() > 100) {
      String s = first;
      while (true) {
        String a = StringUtils.substring(s, 0, 100);
        list.add(a);
        String b = " |             " + StringUtils.substring(s, 100);
        s = b;
        if (b.length() <= 100) {
          list.add(b);
          break;
        }
      }
    } else {
      list.add(first);
    }
    return list;
  }

  private static List<String> generateLocation(String location) {
    List<String> list = new LinkedList<>();
    String first = " |- location:  " + location;
    if (first.length() > 100) {
      String s = first;
      while (true) {
        String a = StringUtils.substring(s, 0, 100);
        list.add(a);
        String b = " |             " + StringUtils.substring(s, 100);
        s = b;
        if (b.length() <= 100) {
          list.add(b);
          break;
        }
      }
    } else {
      list.add(first);
    }
    return list;
  }

  private static String[] getHeaders(int i, Flight flight, Instruction instruction) {
    if (instruction.id() == Instruction.Id.DOT) {
      if (i != flight.getInstructions().size() - 1)
        return new String[]{"      |- (DOT) ", "      |        "};
      else
        return new String[]{"      \\- (DOT) ", "               "};
    } else {
      if (i != flight.getInstructions().size() - 1)
        return new String[]{
            "      |- (" + instruction.id() + ") ",
            "      |        "};
      else
        return new String[]{
            "      \\- (" + instruction.id() + ") ",
            "               "};
    }
  }
}
