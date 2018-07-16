/*
 * Copyright (c) 2017, ZoltanTheHun
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package skyhussars.engine.gamestates;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OptionsManager {

    private static final Logger log = LoggerFactory.getLogger(OptionsManager.class);
    private final String location;
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new Jdk8Module());
    private final File file;

    public OptionsManager(String optionsLocation) {
        location = optionsLocation;
        file = new File(location + "/options.txt");
    }

    public Options loadOptionsFromFileSystem() {
        log.info("Loading options from: " + file.getAbsolutePath());
        Options options = new Options();
        try {
            if (!file.exists()) {
                log.info("Option file does not exist. Creating new one...");
                mapper.writerWithDefaultPrettyPrinter().writeValue(file, options);
            } else {
                log.info("Reading options file");
                options = mapper.readValue(file, Options.class);
            }
        } catch (IOException ex) {
            log.error("Exception while loading options.", ex);
        }
        return options;
    }

    public void persistOptions(Options options) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, options);
        } catch (IOException ex) {
            log.error("Exception while persisting options.", ex);
        }
    }

}
