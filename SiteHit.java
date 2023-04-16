public class SiteHit {
    public Url webpage;
    public int hitCount;

    public SiteHit() {
        webpage = null;
        hitCount = 0;
    }

    public SiteHit(Url siteIn) {
        webpage = siteIn;
        hitCount = 1;
    }

    @Override
    public boolean equals(Object o) {
        return (this.webpage.urlLink.equals(((SiteHit) o).webpage.urlLink));
    }
}
