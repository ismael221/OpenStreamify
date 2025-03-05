const CACHE_NAME = "streaming-cache-v1";
const urlsToCache = [
    "/",
    "/css/style.css",
    "/js/script.js",
    "/images/android-chrome-192x192.png",
    "/images/logo.png"
];

self.addEventListener("install", event => {
    event.waitUntil(
        caches.open(CACHE_NAME).then(cache => {
            return cache.addAll(urlsToCache);
        })
    );
});

self.addEventListener("fetch", event => {
    event.respondWith(
        caches.match(event.request).then(response => {
            return response || fetch(event.request);
        })
    );
});
