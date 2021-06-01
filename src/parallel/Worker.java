/*
This file is part of Simple Videogame Resource Manager.

Simple Videogame Resource Manager is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Simple Videogame Resource Manager is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Simple Videogame Resource Manager.  If not, see <https://www.gnu.org/licenses/>.
*/
package parallel;

import java.util.ArrayList;
import parallel.Analysis;

public class Worker implements Runnable {
	private Analysis kb;
        private int start, end;

	public Worker(Analysis kb, int start, int end) {
		this.kb = kb;
                this.start = start;
                this.end = end;
	}

	@Override
	public void run() {
		String threadName = Thread.currentThread().getName();
		String path[];
		String destination;
		int length;
                for (int i = start; i < end; i++) {
			path = kb.getPaths().get(i);
			length = path.length;
                        synchronized(kb.getSortedRoutes()){
                                if(!kb.getSortedRoutes().containsKey(length)){
                                        kb.getSortedRoutes().put(length, new ArrayList<String[]>());
                                }
                        }
                        kb.getSortedRoutes().get(length).add(path);
		}
	}
}
