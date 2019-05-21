package imageApproximation.organisms;

import imageApproximation.graphics.shapes.BasicShape;

import java.util.List;

public class MockOrganism implements OrganismInterface {
    @Override
    public OrganismInterface spawnMutant() {
        return new MockOrganism();
    }

    @Override
    public OrganismInterface crossBreed(OrganismInterface mate) {
        return new MockOrganism();
    }

    @Override
    public List<BasicShape> getGenome() {
        return null;
    }

    @Override
    public OrganismInterface cloneOrganism() {
        return this;
    }
}
