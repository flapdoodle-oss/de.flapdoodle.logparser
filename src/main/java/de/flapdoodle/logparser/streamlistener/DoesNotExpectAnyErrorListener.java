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
package de.flapdoodle.logparser.streamlistener;

import de.flapdoodle.logparser.IStreamErrorListener;
import de.flapdoodle.logparser.StreamProcessException;

/**
 * TODO mmosmann: document class purpose
 * <p/>
 * 
 * @author mmosmann
 */
public class DoesNotExpectAnyErrorListener implements IStreamErrorListener {

	@Override
	public void error(StreamProcessException sx) {
		throw new RuntimeException("did not expect this", sx);
	}
}
