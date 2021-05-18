% consult('queries.pl').
% filter_resource(armoranth,Result).
% shortest_path(hebra_mountains,hyrule_ridge,R).
route(hebra_mountains,tabantha_tundra).
route(hebra_mountains,tabantha_frontier).
route(tabantha_frontier,hyrule_ridge).
route(tabantha_frontier,gerudo_highlands).
route(gerudo_highlands,gerudo_desert).
route(gerudo_desert,faron_grasslands).
route(hyrule_ridge,hyrule_field).
route(tabantha_tundra,great_hyrule_forest).
route(great_hyrule_forest,eldin_mountains).
route(great_hyrule_forest,eldin_canyon).
route(eldin_mountains,death_mountain).
route(eldin_mountains,deep_akkala).
route(eldin_canyon,lanayru_wetlands).
route(eldin_canyon,akkala_highlands).
route(eldin_canyon,death_mountain).
route(deep_akkala,akkala_sea).
route(deep_akkala,akkala_highlands).
route(akkala_highlands,lanayru_sea).
route(faron_grasslands,faron_sea).
route(faron_grasslands,lake_hylia).
route(faron_sea,lake_hylia).
route(lake_hylia,hyrule_field).
route(lake_hylia,west_necluda).
route(lake_hylia,east_necluda).
route(lanayru_wetlands,hyrule_field).
route(lanayru_wetlands,west_necluda).
route(lanayru_wetlands,lanayru_great_spring).
route(lanayru_sea,lanayru_great_spring).
route(lanayru_sea,mount_lanayru).
route(lanayru_great_spring,mount_lanayru).
route(west_necluda,east_necluda).
route(east_necluda,mount_lanayru).
route(east_necluda,necluda_sea).
route(mount_lanayru,necluda_sea).
route(tabantha_tundra,hebra_mountains).
route(tabantha_frontier,hebra_mountains).
route(hyrule_ridge,tabantha_frontier).
route(gerudo_highlands,tabantha_frontier).
route(gerudo_desert,gerudo_highlands).
route(faron_grasslands,gerudo_desert).
route(hyrule_field,hyrule_ridge).
route(great_hyrule_forest,tabantha_tundra).
route(eldin_mountains,great_hyrule_forest).
route(eldin_canyon,great_hyrule_forest).
route(death_mountain,eldin_mountains).
route(deep_akkala,eldin_mountains).
route(lanayru_wetlands,eldin_canyon).
route(akkala_highlands,eldin_canyon).
route(death_mountain,eldin_canyon).
route(akkala_sea,deep_akkala).
route(akkala_highlands,deep_akkala).
route(lanayru_sea,akkala_highlands).
route(faron_sea,faron_grasslands).
route(lake_hylia,faron_grasslands).
route(lake_hylia,faron_sea).
route(hyrule_field,lake_hylia).
route(west_necluda,lake_hylia).
route(east_necluda,lake_hylia).
route(hyrule_field,lanayru_wetlands).
route(west_necluda,lanayru_wetlands).
route(lanayru_great_spring,lanayru_wetlands).
route(lanayru_great_spring,lanayru_sea).
route(mount_lanayru,lanayru_sea).
route(mount_lanayru,lanayru_great_spring).
route(east_necluda,west_necluda).
route(mount_lanayru,east_necluda).
route(necluda_sea,east_necluda).
route(necluda_sea,mount_lanayru).
route(akkala_sea,akkala_highlands).
route(akkala_highlands,akkala_sea).
route(akkala_sea,hyrule_ridge).
route(hyrule_ridge,akkala_sea).


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