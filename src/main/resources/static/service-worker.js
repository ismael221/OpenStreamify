const CACHE_NAME = "streaming-cache-v1";
const urlsToCache = [
    "/",
    "/list",
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

self.addEventListener("push", event => {
    const data = event.data.json();
    self.registration.showNotification(data.title, {
        body: data.body,
        icon: "/icons/icon-192.png"
    });
});