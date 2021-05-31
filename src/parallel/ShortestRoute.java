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
import javafx.scene.control.Alert;
import parallel.Analysis;
import prolog.PrologQuery;

public class ShortestRoute implements Runnable {
	private Analysis kb;
        private String origin;
        private Graph map;
        private int start, end, numberOfDestinations, i;
	
        public ShortestRoute(Analysis kb, Graph map, String origin, int start, int end) {
		this.kb = kb;
                this.origin = origin;
                this.start = start;
                this.end = end;
                this.map = map;
                numberOfDestinations = kb.getLandmarks().length;
	}
        
        public ShortestRoute(Analysis kb, String origin, int i) {
		this.kb = kb;
                this.origin = origin;
                numberOfDestinations = kb.getLandmarks().length;
                this.i = i;
	}

	@Override
	public void run() {
		String threadName = Thread.currentThread().getName();
		String path[];
		String destination;
                try {
                        for (i = start; i < end; i++) {
                                if(i < numberOfDestinations){
                                        destination = kb.getLandmarks()[i];
                                        if(origin.equals(destination)){
                                                String[] aux = {origin};
                                                path = aux;                                         
                                        }else{
                                                path = map.shortestPath(origin,destination).toArray(new String[0]);
                                        }
                                        kb.getPaths().set(i, path);
                                }
                        }
                } catch (Exception e) {
                        System.out.printf("Exception in thread %s, length = %d, start = %d, end = %d\n", threadName, numberOfDestinations, start, end);
                        synchronized(kb){
                                kb.setCannotReachDestinations(true);
                        }
                }
	}
}
