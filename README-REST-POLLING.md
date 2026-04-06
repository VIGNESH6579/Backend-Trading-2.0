# Backend Trading 2.0 - REST Polling Edition

## Overview
This is the REST Polling version of the Backend Trading 2.0 project, optimized for deployment on platforms like Render that restrict outbound WebSocket connections.

## Key Changes
- **Removed WebSocket Dependencies**: Eliminated WebSocket V2 implementation that caused "network unreachable" errors
- **Added REST Polling**: Implemented 2-second interval polling to Angel One REST APIs
- **Spring Boot Scheduling**: Added `@EnableScheduling` for automated polling
- **Same API Endpoints**: Frontend can continue using the same endpoints without changes

## Architecture

### REST Polling Service
- Polls Angel One REST APIs every 2 seconds
- Fetches option chain data, market prices, and OI updates
- Broadcasts data to frontend via existing STOMP WebSocket (browser connection)
- Caches latest data to minimize API calls

### Benefits
✅ **Works on any server** - No outbound WebSocket restrictions  
✅ **No network errors** - Uses standard HTTP/HTTPS  
✅ **Easy to debug** - Simple REST calls  
✅ **Same frontend compatibility** - No changes needed to frontend  

### Limitations
⚠️ **1-2 second delay** - Not truly real-time  
⚠️ **Higher API usage** - More frequent REST calls  
⚠️ **Slightly more load** - Continuous polling  

## API Endpoints

### Authentication
- `POST /api/login` - Login with Angel One credentials
- `POST /api/logout` - Logout and stop polling
- `GET /api/session` - Get session status

### Market Data
- `POST /api/start-feed` - Start REST polling for instrument/expiry
- `GET /api/expiries` - Get available expiry dates
- `POST /api/analyze` - AI analysis of current data

### System
- `GET /api/health` - Health check

## Deployment

### Render Deployment
1. Connect this repository to Render
2. Set environment variables (if any)
3. Deploy - will work without WebSocket issues

### Local Development
```bash
mvn spring-boot:run
```

## Configuration
- Polling interval: 2 seconds (configurable in `RestPollingService`)
- API rate limits handled automatically
- Automatic reconnection on failures

## Frontend Integration
Your existing frontend will work without any changes. The same `/topic/*` WebSocket endpoints are used for browser communication.

## Monitoring
- Check `/api/health` for service status
- Check `/api/session` for polling status
- Logs show polling activity and errors
