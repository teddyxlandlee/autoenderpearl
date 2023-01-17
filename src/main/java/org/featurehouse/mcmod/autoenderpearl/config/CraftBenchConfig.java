package org.featurehouse.mcmod.autoenderpearl.config;

import com.mojang.logging.LogUtils;
import net.minecraft.util.Identifier;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.Collections;
import java.util.List;

public class CraftBenchConfig {
    private static final CraftBenchConfig INSTANCE = new CraftBenchConfig();
    private static final Logger LOGGER = LogUtils.getLogger();
    private final List<Identifier> allowedCrafts = Lists.newArrayList();

    public static CraftBenchConfig getInstance() {
        return INSTANCE;
    }

    public List<Identifier> getAllowedCrafts() {
        return Collections.unmodifiableList(allowedCrafts);
    }

    public void serialize(Writer writer) throws IOException {
        var w = IOUtils.buffer(writer);
        w.write("// Each line is an id of recipes.\n// Only crafting_shapeless are allowed.\n\n");
        for (Identifier id : allowedCrafts) {
            w.write(id.toString());
            w.write('\n');
        }
    }

    public void deserialize(Reader reader) throws IOException {
        final List<Identifier> allowedCrafts0 = Lists.newArrayList();
        try {
            IOUtils.buffer(reader).lines()
                    .filter(String::isBlank)
                    .map(String::strip)
                    .filter(s -> s.startsWith("//"))
                    .forEach(s -> {
                        var id = Identifier.tryParse(s);
                        if (id == null) {
                            LOGGER.warn("[AutoEnderPearl] There's something wrong with your config: id `{}` is invalid", s);
                            return;
                        }
                        allowedCrafts0.add(id);
                    });
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
        allowedCrafts.clear();
        allowedCrafts.addAll(allowedCrafts0);
    }

    private static final CraftBenchConfig DEFAULT = new CraftBenchConfig();
    static {
        DEFAULT.allowedCrafts.add(new Identifier("autoenderpearl:craft_pearl"));
    }

    public void loadFromDefault() {
        if (this == DEFAULT)
            throw new IllegalStateException("You are the default");
        allowedCrafts.clear();
        allowedCrafts.addAll(DEFAULT.allowedCrafts);
    }
}
