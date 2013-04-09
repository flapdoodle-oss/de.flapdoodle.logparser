/**
 * Copyright (C) 2013
 *   Michael Mosmann <michael@mosmann.de>
 *
 * with contributions from
 * 	${lic.developers}
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.flapdoodle.logparser.stacktrace;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * TODO mmosmann: document class purpose
 * <p/>
 *
 * @author mmosmann
 */
public class StackLines {
    protected final ImmutableList<At> _stack;
    private More _more;

    public StackLines(List<At> stack,More more) {
        _stack = ImmutableList.copyOf(stack);
        _more=more;
    }

    public Optional<At> firstAt() {
        return !_stack.isEmpty() ? Optional.of(_stack.get(0)) : Optional.<At>absent();
    }

    public Optional<More> more() {
        return Optional.fromNullable(_more);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (At at : _stack) {
            sb.append(at);
            sb.append("\n");
        }
        if (_more != null) {
            sb.append(_more);
            sb.append("\n");
        }
        return sb.toString();
    }

}
