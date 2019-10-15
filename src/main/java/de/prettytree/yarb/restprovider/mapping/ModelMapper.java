package de.prettytree.yarb.restprovider.mapping;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public abstract interface ModelMapper<SourceMode, TargetModel> {
	
	TargetModel map(SourceMode sourceModel);
}
