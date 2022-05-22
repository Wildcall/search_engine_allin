package ru.malygin.searcher.searcher.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
public class Lemmantisator {

    /**
     * Метод разбивает исходный текст на отдельные леммы, и возвращает список лем.
     *
     * @param text исходный текст
     * @return List<String>
     */
    public static List<String> getLemmas(String text,
                                         AlphabetType alphabetType) {
        return getWordsRank(text, 1.0, alphabetType)
                .keySet()
                .stream()
                .toList();
    }

    /**
     * Метод разбивает исходный текст на отдельные слова(леммы), считает количество одинаковых слов(лемм) и умножает количество на весовой коэффициент.
     * Возвращает пары слова и её суммарного веса в данном тексте
     *
     * @param text   исходный текст
     * @param weight весовой коэффициент для данного текста
     * @return Map<String, Double>
     */
    private static Map<String, Double> getWordsRank(String text,
                                                    Double weight,
                                                    AlphabetType alphabetType) {
        Map<String, Double> lemmas = new HashMap<>();
        try {
            LuceneMorphology luceneMorphology = new RussianLuceneMorphology();
            getCyrillicText(text).forEach(item -> {
                if (item.isEmpty() || item.isBlank()) return;
                luceneMorphology
                        .getNormalForms(item)
                        .forEach(word -> {
                            List<String> info = luceneMorphology.getMorphInfo(word);
                            boolean skipWord = false;
                            for (String tmp : info) {
                                if (tmp.contains("СОЮЗ") || tmp.contains("МЕЖД") || tmp.contains("ПРЕДЛ")) {
                                    skipWord = true;
                                    break;
                                }
                            }
                            if (!skipWord) {
                                lemmas.compute(word, (key, val) -> (val == null) ? weight : val + weight);
                            }
                        });
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lemmas;
    }

    /**
     * Выполняет удаление из теста все символов отличных от кириллицы
     *
     * @param text исходный текст
     * @return Stream
     */
    private static Stream<String> getCyrillicText(String text) {
        return Arrays.stream(text
                                     .replaceAll("[^А-Яа-я\\s]", "")
                                     .replaceAll("(\\s)+", " ")
                                     .toLowerCase(Locale.ROOT)
                                     .split(" "));
    }
}
