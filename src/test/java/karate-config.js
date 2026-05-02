function karateConfig() {
  const config = {
    local: {
      url: 'http://localhost:5001',
    },
    dev: {
      url: 'http://api.opencambio.org',
    }
  }

  const env = karate.env || 'dev';
  return { ...config[env], env };
}