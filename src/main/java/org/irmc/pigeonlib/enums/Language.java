package org.irmc.pigeonlib.enums;

import com.google.common.base.Strings;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * Language codes and their corresponding locales.
 *
 * <p>
 *      <h2>Examples</h2>
 *          {@link #EN_US} - English (United States) <br>
 *          {@link #ZH_CN} - Chinese (China) <br>
 *          {@link #ARABIC} - Arabic <br>
 *          {@link #BULGARIAN} - Bulgarian <br>
 *          {@link #CZECH} - Czech <br>
 *      <h2>Formats</h2>
 *          1. <code>%LANGUAGE%_%REGION%</code> - For example, `EN_US` for English (United States) <br>
 *          2. <code>%LANGUAGE%</code> - For example, `ARABIC` for Arabic
 *
 *          <h4>Explanation</h4>
 *          <code>%LANGUAGE%</code> is the language code in ISO 639-1 format <br>
 *          <code>%REGION%</code> is the region code in ISO 3166-1 alpha-2 format
 *      <h2>Note</h2>
 *          - The language codes are case-insensitive and in lower case.<br>
 *          - The region codes are case-insensitive and in upper case(see {@link #toTagRegionUpper()}) or lower case(see {@link #toTag()}).
 * </p>
 */
public enum Language {
    EN_US(Locale.US),
    EN_GB(Locale.UK),
    ZH_CN(Locale.SIMPLIFIED_CHINESE),
    ZH_TW(Locale.TRADITIONAL_CHINESE),
    ZH_HK("zh", "hk"),
    ARABIC("ar"),
    BULGARIAN("bg"),
    CZECH("cs"),
    DANISH("da"),
    DUTCH("nl"),
    FINNISH("fi"),

    /**
     * universal language code for German
     */
    DE(Locale.GERMAN),
    DE_DE(Locale.GERMANY),
    ES("es", "es"),
    /**
     * universal language code for French
     */
    FR(Locale.FRENCH),
    FR_FR(Locale.FRANCE),
    /**
     * universal language code for Italian
     */
    IT(Locale.ITALIAN),
    IT_IT(Locale.ITALY),
    /**
     * universal language code for Japanese
     */
    JA(Locale.JAPANESE),
    JA_JP(Locale.JAPAN),
    /**
     * universal language code for Korean
     */
    KO(Locale.KOREAN),
    KO_KR(Locale.KOREA),
    /**
     * universal language code for Russian
     */
    RU("ru"),
    RU_RU("ru", "ru"),
    ;

    private final String language;
    private final String region;

    Language(Locale locale) {
        this.language = locale.getLanguage().toLowerCase();
        this.region = locale.getCountry().toLowerCase();
    }

    Language(String language) {
        this(language, null);
    }

    Language(String country, @Nullable String region) {
        this.language = country.toLowerCase();
        this.region = region;
    }

    public final String toTag() {
        return language + (Strings.isNullOrEmpty(region) ? "_" + region.toLowerCase() : "");
    }

    public final String toTagRegionUpper() {
        if (Strings.isNullOrEmpty(region)) {
            return toTag();
        }

        return toTag() + "_" + region.toUpperCase();
    }

    /**
     * Get the language code of the {@link Language}.
     * (in lower case)
     * Represented the language code in ISO 639-1 format.
     * @return the language code of the {@link Language}
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Get the region code of the {@link Language}.
     * (in lower case)
     * Represented the region code in ISO 3166-1 alpha-2 format.
     * @return the region code of the {@link Language}, or null if the language has no region
     */
    @Nullable
    public String getRegion() {
        return region == null ? null : region.toLowerCase();
    }

    public final Locale toJavaLocale() {
        Locale.Builder builder = new Locale.Builder();

        builder.setLanguage(language);

        if (!Strings.isNullOrEmpty(region)) {
            builder.setRegion(region);
        }

        return builder.build();
    }
}
