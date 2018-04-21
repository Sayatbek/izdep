package com.izdep.app.runner.stemmer;

import com.izdep.app.runner.utils.ApiConst;
import com.izdep.app.runner.utils.DBHelper;
import com.izdep.app.runner.utils.PropertyHelper;
import org.jsoup.helper.StringUtil;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IzdepStemmer {

    private static final Pattern PERFECTIVEGROUND = Pattern.compile("(а|е|й|ып|іп|п|ғалы|гелі|қалы|келі|қан|ған|кен|ген|ар|ер|р|мақ|мек|бақ|бек|пақ|пек)$");
    private static final Pattern REFLEXIVE = Pattern.compile("(н|ын|ін|л|ыл|іл|дыр|дір|тыр|тір|ғыз|гіз|қыз|кіз|с|ыс|іс)$");
    private static final Pattern ADJECTIVE = Pattern.compile("(лы|лі|ды|ді|ты|ті|сыз|сіз|ғы|гі|қы|кі|лық|лік|дық|дік|тық|тік|шыл|шіл|шаң|шең|дай|дей|тай|тей|қой|қор|паз|кер|гер|и|ы|і)$");
    private static final Pattern PARTICIPLE = Pattern.compile("(атын|етін|йтын|йтін|мын|мін|бын|бін|пын|пін|мыз|міз|быз|біз|сың|сің|сыңдар|сіңдер|сыз|сіз|сіздер|сыздар)$");
    private static final Pattern VERB = Pattern.compile("(ла|ле|да|де|та|те|лан|лен|дан|ден|тан|тен|лас|лес|дас|дес|тас|тес|лат|лет|дат|дет|а|е|ай|ей|й|қар|ғар|кер|гер|ар|ер|р|ал|әл|ыл|іл|л|ық|ік|сы|сі|ымсы|імсі|сын|сін|сыра|сіре|ра|ре|ыра|іре|ырай|ірей|ды|ді)$");
    private static final Pattern NOUN = Pattern.compile("(ым|ім|м|ың|ың|ң|ыңыз|іңіз|ңыз|ңіз|сы|сі|ы|і|ымыз|іміз|мыз|міз|ың|ің|ыңыз|іңіз|ы|і|дар|лер|лар|дер|тар|тер|ның|нің|дың|дің|тың|тің|ға|ге|қа|ке|ды|ді|ты|ті|да|де|та|те|нан|нен|дан|ден|тан|тен|мен)$");
    private static final Pattern RVRE = Pattern.compile("^(.*?[аоеыіоөұүу])(.*)$");
    private static final Pattern DERIVATIONAL = Pattern.compile(".*[^аоеыіоөұүу]+[аоеыіоөұүу].*ость?$");
    private static final Pattern DER = Pattern.compile("(лық|лік|дық|дік|тық|тік)$");
    private static final Pattern SUPERLATIVE = Pattern.compile("(рақ|рек|ырақ|ірек|лау|леу|дау|деу|тау|теу|ғыл|ғылт|қыл|қылт|ғылтым|қылтым|шыл|шіл|шылтым|шілтім|ғыш|ілдір|аң|қай|ша|ше|)$");

    private static final Pattern I = Pattern.compile("и$");
    private static final Pattern P = Pattern.compile("ь$");
    private static final Pattern NN = Pattern.compile("нн$");

    public static String stem(String word) {
        int i = 0;
        word = word.toLowerCase();
        word = word.replace('ё', 'е');
        Matcher m = RVRE.matcher(word);
        if (m.matches()) {
            String pre = m.group(1);
            String rv = m.group(2);
            String temp = PERFECTIVEGROUND.matcher(rv).replaceFirst("");

            if (temp.equals(rv)) {
                rv = REFLEXIVE.matcher(rv).replaceFirst("");

                temp = ADJECTIVE.matcher(rv).replaceFirst("");

                if (!temp.equals(rv)) {
                    rv = temp;
                    rv = PARTICIPLE.matcher(rv).replaceFirst("");
                } else {
                    temp = VERB.matcher(rv).replaceFirst("");

                    if (temp.equals(rv)) {
                        rv = NOUN.matcher(rv).replaceFirst("");
                    } else {
                        rv = temp;
                    }
                }

            } else {
                rv = temp;
            }

            rv = I.matcher(rv).replaceFirst("");

            if (DERIVATIONAL.matcher(rv).matches()) {
                rv = DER.matcher(rv).replaceFirst("");
            }

            temp = P.matcher(rv).replaceFirst("");


            if (temp.equals(rv)) {
                rv = SUPERLATIVE.matcher(rv).replaceFirst("");

                rv = NN.matcher(rv).replaceFirst("н");

            }else{
                rv = temp;
            }
            word = pre + rv;

        }

        return word;
    }
}
