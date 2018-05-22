package com.loopperfect.buckaroo.zalogit2;

import com.google.common.base.Preconditions;
import com.loopperfect.buckaroo.GitCommitHash;
import com.loopperfect.buckaroo.GitProvider;
import com.loopperfect.buckaroo.Identifier;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.Optional;

public final class ZaloGit2GitProvider implements GitProvider {

    private ZaloGit2GitProvider() {

    }

    @Override
    public Identifier recipeIdentifierPrefix() {
        return Identifier.of("zalogit2");
    }

    @Override
    public String gitURL(final Identifier owner, final Identifier project) {
        return "https://zalogit2.zing.vn/" + owner.name + "/" + project.name + ".git";
    }

    @Override
    public URI projectURL(final Identifier owner, final Identifier project) {
        Preconditions.checkNotNull(owner);
        Preconditions.checkNotNull(project);
        try {
            return new URI("https://zalogit2.zing.vn/" + owner.name + "/" + project.name);
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public URI zipURL(final Identifier owner, final Identifier project, final GitCommitHash commit) {
        try {
            return new URI("https://zalogit2.zing.vn/" + owner.name + "/" + project.name + "/repository/archive.zip?ref=" + commit.hash.substring(0, 40));
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<String> zipSubPath(final Identifier owner, final Identifier project, final GitCommitHash commit) {
        Preconditions.checkNotNull(owner);
        Preconditions.checkNotNull(project);
        Preconditions.checkNotNull(commit);
        return Optional.of(project.name + "-" + commit.hash + "-" + commit.hash);
    }

    public static ZaloGit2GitProvider of() {
        return new ZaloGit2GitProvider();
    }
}
