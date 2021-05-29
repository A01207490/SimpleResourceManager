:-include('resources.pl').
:-include('landmarks.pl').
:-include('locations.pl').
:-include('routes.pl').
:-include('routes-java.pl').
:-include('recipes.pl').
:-use_module(library(lists)). 
% consult('queries.pl').
getRecipes(Name,Stat,Potency,Duration,Resources,[Name,Stat,Potency,Duration,Resources]):-
    recipe(Name,Stat,Potency,Duration,Resources).
getLocations(Resource,Landmarks,[Resource,Landmarks]):-
    location(Resource,Landmarks).
getLandmarks(Landmark):-
    landmark(Landmark).
getResources(Resource):-
    resource(Resource).
getRoutesJava([Origin,Destionation]):-
    route_java(Origin,Destionation).
getLandmarksWhereResourceIsFound(Resource,Landmarks):-
    location(LocationResource,Landmarks),
    Resource == LocationResource,!.
getLandmarksWhereResourceIsFound(_,[]):-!.
getLandmarksUsedInRoutes(Landmark):-
    route(Landmark,_).
getResourcesFoundInLandmark(Landmark,Resource):-
    location(Resource,Landmarks),
    member(Landmark,Landmarks).
isLandmarkUsedInRoutes(Landmark):-
    route(Origin,_),
    Landmark == Origin,!.
isLandmarkUsedInRoutes(Landmark):-
    route(_,Destination),
    Landmark == Destination,!.
isLandmarkUsedInLocations(Landmark):-
    location(_,Landmarks),
    member(Landmark,Landmarks),!.
isResourceUsedInLocations(Resource):-
    location(Resource,Landmarks),
    not(Landmarks == []),!.
isEntryAResource(Entry):-
    resource(Resource),
    Entry == Resource,!.
isEntryALandmark(Entry):-
    landmark(Landmark),
    Entry == Landmark,!.
getResourcesThatHaveLocations(Resource):-
    location(Resource,Landmarks),
    not(Landmarks == []).
% This one isn't used
getDestinationsOfLandmark(Landmark,Destination):-
    route(Landmark,Destination).
getListOfDestinationsOfLandmark(Landmark,Destinations):-
    findall(Destination,route(Landmark,Destination),Destinations).
getAvailableLandmarks(Landmark,AvailableLandmark):-
    getListOfDestinationsOfLandmark(Landmark,Destinations),
    landmark(AvailableLandmark),
    not(AvailableLandmark == Landmark),
    not(member(AvailableLandmark,Destinations)).
shortestRoute(X,Y,P):-
    bfs4(X,Y,P).
% this one gets stuck
bfs1(X,Y,P):-
    bfs_a(Y,[n(X,[])],R),
    reverse(R,P).    
bfs_a(Y,[n(Y,P)|_],P).
bfs_a(Y,[n(S,P1)|Ns],P) :-
    findall(n(S1,[S1|P1]),route(S,S1),Es),
    append(Ns,Es,O),
    bfs_a(Y,O,P).
bfs2(X,Y,P) :-
    bfs_b(Y,[n(X,[])],[],R),
    reverse(R,P).
    bfs_b(Y,[n(Y,P)|_],_,P).
bfs_b(Y,[n(S,P1)|Ns],C,P) :-
    findall(n(S1,[S1|P1]),
    (route(S,S1),
    \+ member(S1,C)),
    Es),
    append(Ns,Es,O),
    bfs_b(Y,O,[S|C],P). 
bfs3(X,Y,P) :-
    bfs_c(Y,[n(X,[])],[n(X,[])],R),
    reverse(R,P).
    bfs_c(Y,[n(Y,P)|_],_,P).
bfs_c(Y,[n(S,P1)|Ns],C,P) :-
    findall(n(S1,[S1|P1]),
    (route(S,S1),
    \+ member(n(S1,_),C)),
    Es),
    append(Ns,Es,O),
    append(C,Es,C1),
    bfs_c(Y,O,C1,P).
% this one works
bfs4(X,Y,P):-
    bfs_d(Y,[n(X,[])],[],R),
    reverse(R,P).
    bfs_d(Y,[n(Y,P)|_],_,P).
bfs_d(Y,[n(S,P1)|Ns],C,P) :-
    length(P1,L),
    findall(n(S1,[S1|P1]),
    (route(S,S1),
    \+ (member(n(S1,P2),Ns), length(P2,L)),
    \+ member(S1,C)),
    Es),
    append(Ns,Es,O),
    bfs_d(Y,O,[S|C],P).
path(Origin,Final_Destination,Path,_Crossed):-
    route(Origin,Final_Destination),
    Aux = [Final_Destination|[]],
    Path = [Origin|Aux].
path(Origin,Final_Destination,Path,Crossed):-
    route(Origin,In_Between_Destination),
    not(member(In_Between_Destination,Crossed)),
    UpdatedCrossed = [In_Between_Destination|Crossed],
    path(In_Between_Destination,Final_Destination,PreviousPath,UpdatedCrossed),
    Path = [Origin|PreviousPath].
isDestinationReachable(Origin,Final_Destination,Path):-
    path(Origin,Final_Destination,Path,[Origin|[]]),!.
count([_|Rest],Result):-
    Rest == [],
    Result = 1;
    count(Rest,ResultAux),
    Result is ResultAux + 1.
getSumLocations(Sum):-
    location(_,Landmarks),
    count(Landmarks,Sum).
% consult('queries.pl').