/*
 *************************************************************************
 * Copyright 2013 DFG SPP 1593 (http://dfg-spp1593.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *************************************************************************
 */

package org.cocome.tradingsystem.inventory.parser.plant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cocome.tradingsystem.inventory.application.plant.expression.ExpressionInfo;
import org.cocome.tradingsystem.inventory.application.plant.expression.MarkupInfo;

import java.io.IOException;

/**
 * Parser utility for handling plant recipe instructions
 *
 * @author Rudolf Biczok
 */
public class MarkupParser {

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Parses the given JSON string and returns a list of {@link ExpressionInfo} from the parsed content
     *
     * @param text the JSON string
     * @return a list of extracted {@link ExpressionInfo}
     */
    public MarkupInfo parse(final String text) throws IOException {
        return mapper.readValue(text, MarkupInfo.class);
    }

    /**
     * Converts the given list of {@link ExpressionInfo} to a JSON string
     *
     * @param plantOperationInfo the list of expressions
     * @return the JSON string representation of the given list of expressions
     */
    public String toString(final MarkupInfo plantOperationInfo) throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(plantOperationInfo);
    }

}
