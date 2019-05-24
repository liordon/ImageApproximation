package imageApproximation.organisms;

import imageApproximation.graphics.shapes.BasicShape;

import java.util.List;

/**
 * Used for organisms in a genetic algorithm.
 * organisms should be pretty much immutable (apart from caching) so all operations on them create
 * new copies.
 */
public interface OrganismInterface extends Cloneable {
    OrganismInterface spawnMutant();

    OrganismInterface crossBreed(OrganismInterface... mates);

    List<BasicShape> getGenome();

    OrganismInterface cloneOrganism();
}
