using Microsoft.AspNetCore.Mvc;
using GymTrackerAPI.DataTypes;
using GymTrackerAPI.Data;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;

namespace GymTrackerAPI.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class UserController : ControllerBase
    {
        private readonly AppDbContext _context;

        public UserController(AppDbContext context)
        {
            _context = context;
        }

        [HttpGet(Name = "GetUsers")]
        public async Task<IEnumerable<User>> Get()
        {
            return await _context.User.ToListAsync();
        }
    }
}