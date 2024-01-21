using GymTrackerAPI.DataTypes;
using Microsoft.EntityFrameworkCore;

namespace GymTrackerAPI.Data
{
    public class AppDbContext : DbContext
    {
        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options)
        {
        }

        public DbSet<User> User { get; set; }
    }
}