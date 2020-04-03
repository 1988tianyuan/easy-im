import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Filter;
import java.util.stream.Collectors;

public class Test {

    public static void main(String[] args) throws IOException {
        List<String> log = FileUtils.readLines(new File("im-server/src/main/resources/test.log"), Charset.defaultCharset());
        log.parallelStream().filter(s -> s.contains("ERROR"))
           .sorted()
           .collect(Collectors.groupingBy(s -> s)).entrySet().parallelStream()
           .map(entry -> new GroupResult(entry.getValue().size(), entry.getKey()))
           .sorted()
           .collect(Collectors.toList())
           .forEach(System.out::println);
    }

    private static class GroupResult implements Comparable<GroupResult> {
        public GroupResult(int count, String text) {
            this.count = count;
            this.text = text;
        }

        private int count;
        private String text;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return "GroupResult{" +
                    "count=" + count +
                    ", text='" + text + '\'' +
                    '}';
        }

        @Override
        public int compareTo(GroupResult groupResult) {
            return groupResult.getCount() - count;
        }
    }
}
