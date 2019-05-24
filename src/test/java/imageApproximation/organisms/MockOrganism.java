package imageApproximation.organisms;

import imageApproximation.graphics.shapes.BasicShape;

import java.util.Collections;
import java.util.List;

public class MockOrganism implements OrganismInterface {
    @Override
    public OrganismInterface spawnMutant() {
        return new MockOrganism();
    }

    @Override
    public OrganismInterface crossBreed(OrganismInterface... mates) {
        return new MockOrganism();
    }

    @Override
    public List<BasicShape> getGenome() {
        return Collections.emptyList();
    }

    @Override
    public OrganismInterface cloneOrganism() {
        return this;
    }
}
