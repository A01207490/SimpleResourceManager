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

import parallel.Analysis;

public class Worker implements Runnable {
	private Analysis kb;

	public Worker(Analysis kb) {
		this.kb = kb;
	}

	@Override
	public void run() {
		String threadName = Thread.currentThread().getName();
		String path[];
		String destination;
		int length;
		// System.out.println(kb.getPaths().size() / 2);
		for (int i = 0; i < kb.getPaths().size() / 2; i++) {
			path = kb.getPaths().get(i);
			length = path.length;
			if ((i % 2 == 0) && threadName == "Pair") {
				if (i == 0) {
					kb.setPair_len(length);
					kb.setPair_pos(i);
				} else {
					if (length <= kb.getPair_len()) {
						kb.setPair_len(length);
						kb.setPair_pos(i);
					}
				}
			} else if ((i % 2 != 0) && threadName == "Odd") {
				if (i == 1) {
					kb.setOdd_len(length);
					kb.setOdd_pos(i);
				} else {
					if (length <= kb.getOdd_len()) {
						kb.setOdd_len(length);
						kb.setOdd_pos(i);
					}
				}
			}

		}
	}

}
