:-include('locations2.pl').
count([_|Rest],Result):-
    Rest == [],
    Result = 1;
    count(Rest,ResultAux),
    Result is ResultAux + 1.
getSumLocations(Sum):-
    location(_,Landmarks),
    count(Landmarks,Sum).

