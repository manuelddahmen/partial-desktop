module partial.desktop {
    requires java.desktop;
    requires commons.mp;
    requires java.logging;
    // Utiliser 'requires static' permet de lire le module automatique
    // même s'il n'est pas complètement compatible JPMS

    exports one.empty3.libs;
}