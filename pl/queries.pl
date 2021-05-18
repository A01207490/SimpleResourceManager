:-include('resources.pl').
:-include('landmarks.pl').
:-include('locations.pl').
:-include('routes.pl').
:-include('recipes.pl').
% consult('queries.pl').
getRecipes(Name,Stat,Potency,Duration,Resources,[Name,Stat,Potency,Duration,Resources]):-
    recipe(Name,Stat,Potency,Duration,Resources).
getLocations(Resource,Landmarks,[Resource,Landmarks]):-
    location(Resource,Landmarks).
getLandmarks(Landmark):-
    landmark(Landmark).
getResources(Resource):-
    resource(Resource).
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
    bfs_a(Y,[d(X,[])],R),
    reverse(R,P1),
    P = [X|P1],!.
bfs_a(Y,[d(Y,P)|_],P).
bfs_a(Y,[d(S,P1)|Ns],P) :-
    findall(d(S1,[S1|P1]),route(S,S1),Es),
    append(Ns,Es,O),
    bfs_a(Y,O,P).
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
isDestinationReachable(Origin,Final_Destination):-
    path(Origin,Final_Destination,_,[Origin|[]]),!.
% consult('queries.pl').
