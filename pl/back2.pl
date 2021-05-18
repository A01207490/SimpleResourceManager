:-include('resources.pl').
:-include('landmarks.pl').
:-include('locations.pl').
:-include('routes.pl').
:-include('recipes.pl').
% consult('queries.pl').



build_location(Resource,Landmarks,Location):-
    Location = [Resource,Landmarks|[]].

parse_location([Resource,Landmarks|[]],Resource,Landmarks):-!.

get_locations(Locations):-
    add_location([], Locations).
add_location(PartialResult,Result):-
    location(Resource,Landmarks),
    build_location(Resource,Landmarks,Location),
    not(contains_item(Location,PartialResult)),
    add_location([Location|PartialResult],Result),!.
add_location(PartialResult,PartialResult):-!.

get_landmarks(Landmarks):-
    add_landmark([], Landmarks).
add_landmark(PartialResult,Result):-
    route(Origin,_),
    not(contains_item(Origin,PartialResult)),
    add_landmark([Origin|PartialResult],Result),!.
add_landmark(PartialResult,Result):-
    route(_,Destination),
    not(contains_item(Destination,PartialResult)),
    add_landmark([Destination|PartialResult],Result),!.
add_landmark(PartialResult,PartialResult):-!.

get_resources(Resources):-
    add_resource([], Resources).
add_resource(PartialResult,Result):-
    location(Resource,_),
    not(contains_item(Resource,PartialResult)),
    add_resource([Resource|PartialResult],Result),!.
add_resource(PartialResult,PartialResult):-!.

is_landmark(Landmark):-
    get_landmarks(Landmarks),
    contains_item(Landmark,Landmarks),!.

is_resource(Resource):-
    get_resources(Resources),
    contains_item(Resource,Resources),!.

filter_landmark(Landmark,Result):-
    get_locations(Locations),
    filter_landmark_helper(Landmark,Locations,[],Result),!.
filter_landmark_helper(_Landmark,[],PartialResult,PartialResult):-!.
filter_landmark_helper(Landmark,[Location|Rest],PartialResult,Result):-
    parse_location(Location,Resource,Landmarks),
    member(Landmark,Landmarks),
    filter_landmark_helper(Landmark,Rest,[Resource|PartialResult],Result),!.
filter_landmark_helper(Landmark,[_Location|Rest],PartialResult,Result):-
    filter_landmark_helper(Landmark,Rest,PartialResult,Result),!.

filter_resource(Resource,Result):-
    get_locations(Locations),
    filter_resource_helper(Resource,Locations,Result),!.
filter_resource_helper(_Resource,[],"No matches"):-!.
filter_resource_helper(Resource,[Location|_Rest],Landmarks):-
    parse_location(Location,LocationResource,Landmarks),
    LocationResource == Resource,!.
filter_resource_helper(Resource,[_Location|Rest],Result):-
    filter_resource_helper(Resource,Rest,Result),!.

contains_item(Item,[First|Rest]):-
    Item == First;
    contains_item(Item, Rest).

get_resources_catalog(Resources):-
    add_resources_catalog([], Resources),!.
add_resources_catalog(PartialResult,Result):-
    resource(Resource),
    not(member(Resource,PartialResult)),
    add_resources_catalog([Resource|PartialResult],Result),!.
add_resources_catalog(PartialResult,PartialResult):-!.

get_landmarks_catalog(Landmarks):-
    add_landmarks_catalog([], Landmarks),!.
add_landmarks_catalog(PartialResult,Result):-
    landmark(Landmark),
    not(member(Landmark,PartialResult)),
    add_landmarks_catalog([Landmark|PartialResult],Result),!.
add_landmarks_catalog(PartialResult,PartialResult):-!.

is_landmark_catalog(Landmark):-
    get_landmarks_catalog(Landmarks),
    member(Landmark,Landmarks),!.

is_resource_catalog(Resource):-
    get_resources_catalog(Resources),
    member(Resource,Resources),!.

get_routes(Landmark,Landmarks):-
    add_route(Landmark,[], Landmarks).
add_route(Landmark,PartialResult,Result):-
    route(Origin,Destination),
    Landmark == Origin,
    not(member(Destination,PartialResult)),
    add_route(Landmark,[Destination|PartialResult],Result),!.
add_route(Landmark,PartialResult,Result):-
    route(Origin,Destination),
    Landmark == Destination,
    not(member(Origin,PartialResult)),
    add_route(Landmark,[Origin|PartialResult],Result),!.
add_route(_,PartialResult,PartialResult):-!.

get_available_landmarks(Landmark,AvailableLandmarks):-
    get_routes(Landmark,Destinations),
    get_landmarks_catalog(Landmarks),
    elements_not_shared([Landmark|Destinations],Landmarks,AvailableLandmarks),!.

elements_not_shared(Elements,Catalog,Result):-
    elements_not_shared(Elements,Catalog,[],Result),!.
elements_not_shared(Elements,[First|Rest],PartialResult,Result):-
    not(member(First,Elements)),
    elements_not_shared(Elements,Rest,[First|PartialResult],Result).
elements_not_shared(Elements,[_|Rest],PartialResult,Result):-
    elements_not_shared(Elements,Rest,PartialResult,Result).
elements_not_shared(_,[],PartialResult,PartialResult):-!.
% consult('queries.pl').

is_not_empty([First|_]):-
    not(First == []),!.
resource_used_in_location(Resource):-
    location(Resource,Landmarks),
    is_not_empty(Landmarks),!.
landmark_used_in_location(Landmark):-
    get_landmarks_in_location(Landmarks),
    member(Landmark,Landmarks),!.
get_landmarks_in_location(Result):-
    get_locations(Locations),
    add_location_landmarks(Locations,[], Result).
add_location_landmarks([Location|Rest],PartialResult,Result):-
    parse_location(Location,_,Landmarks),
    elements_not_shared(PartialResult,Landmarks,NewLandmarks),
    append(PartialResult,NewLandmarks,NewPartialResult),
    add_location_landmarks(Rest,NewPartialResult,Result),!.
add_location_landmarks([],PartialResult,PartialResult):-!.


% New Functions.
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
shortest_path(X,Y,P):-
    bfs_a(Y,[d(X,[])],R),
    reverse(R,P1),
    P = [X|P1],!.
bfs_a(Y,[d(Y,P)|_],P).
bfs_a(Y,[d(S,P1)|Ns],P) :-
    findall(d(S1,[S1|P1]),route(S,S1),Es),
    append(Ns,Es,O),
    bfs_a(Y,O,P).
% consult('queries.pl').
