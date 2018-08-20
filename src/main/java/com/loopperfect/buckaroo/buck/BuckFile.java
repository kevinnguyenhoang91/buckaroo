package com.loopperfect.buckaroo.buck;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import com.loopperfect.buckaroo.Either;
import com.loopperfect.buckaroo.Identifier;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Map;

public final class BuckFile {

    private BuckFile() {

    }

    public static Either<IOException, String> generate(final Identifier project) {

        Preconditions.checkNotNull(project);

        final URL url = Resources.getResource("com.loopperfect.buckaroo/ProjectTemplate.mustache");
        final String templateString;
        try {
            templateString = Resources.toString(url, Charsets.UTF_8);
        } catch (final IOException e) {
            return Either.left(e);
        }

        final Map<String, Object> scopes = ImmutableMap.of(
                "name", project.name);

        final Writer writer = new StringWriter();
        final MustacheFactory mustacheFactory = new DefaultMustacheFactory();
        final Mustache mustache = mustacheFactory.compile(new StringReader(templateString), "Project");

        mustache.execute(writer, scopes);

        try {
            writer.flush();
        } catch (final IOException e) {
            return Either.left(e);
        }

        return Either.right(writer.toString());
    }

    public static Either<IOException, String> list(final String name, final ImmutableList<String> values) {

        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(values);

        final URL urlDevelopment = Resources.getResource("com.loopperfect.buckaroo/BuckListTemplate-Development.mustache");
        final String templateStringDevelopment;
        try {
            templateStringDevelopment = Resources.toString(urlDevelopment, Charsets.UTF_8);
        } catch (final IOException e) {
            return Either.left(e);
        }

        final URL urlProduction = Resources.getResource("com.loopperfect.buckaroo/BuckListTemplate-Production.mustache");
        final String templateStringProduction;
        try {
            templateStringProduction = Resources.toString(urlProduction, Charsets.UTF_8);
        } catch (final IOException e) {
            return Either.left(e);
        }

        final Map<String, Object> scopesDevelopment = ImmutableMap.of(
            "name", name + "_DEVELOPMENT",
            "values", values);

        final Writer writerDevelopment = new StringWriter();
        final MustacheFactory mustacheFactoryDevelopment = new DefaultMustacheFactory();
        final Mustache mustacheDevelopment = mustacheFactoryDevelopment.compile(new StringReader(templateStringDevelopment), "BuckListDevelopment");

        mustacheDevelopment.execute(writerDevelopment, scopesDevelopment);

        final Map<String, Object> scopesProduction = ImmutableMap.of(
            "name", name + "_PRODUCTION",
            "values", values);

        final Writer writerProduction = new StringWriter();
        final MustacheFactory mustacheFactoryProduction = new DefaultMustacheFactory();
        final Mustache mustacheProduction = mustacheFactoryProduction.compile(new StringReader(templateStringProduction), "BuckListProduction");

        mustacheProduction.execute(writerProduction, scopesProduction);

        String returnString = writerDevelopment.toString() + "\n" + writerProduction.toString();

        try {
            writerDevelopment.flush();
            writerProduction.flush();
        } catch (final IOException e) {
            return Either.left(e);
        }

        return Either.right(returnString);
    }
}
