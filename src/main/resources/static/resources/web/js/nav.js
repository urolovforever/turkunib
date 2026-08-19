(function(){
    const nav = document.querySelector('nav.site-navbar');
    if (!nav) return;

    const prefersReduced = window.matchMedia('(prefers-reduced-motion: reduce)');
    const MAX_FADE = 350; // px to fully fade background in

    let ticking = false;

    function setBg(y){
        const o = Math.max(0, Math.min(1, y / MAX_FADE));
        nav.style.setProperty('background-color', `rgba(54, 52, 142, ${o.toFixed(2)})`, 'important');
    }

    function onScroll(){
        if (ticking) return;
        ticking = true;
        const y = window.scrollY || document.documentElement.scrollTop || 0;
        if (prefersReduced.matches) {
            // Without animation: jump directly based on position
            setBg(y);
            ticking = false;
            return;
        }
        requestAnimationFrame(() => {
            setBg(y);
            ticking = false;
        });
    }

    // init
    setBg(window.scrollY || document.documentElement.scrollTop || 0);
    document.addEventListener('scroll', onScroll, { passive:true });

})();